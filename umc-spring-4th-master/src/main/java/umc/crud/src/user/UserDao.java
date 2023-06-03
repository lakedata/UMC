package umc.crud.src.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import umc.crud.src.user.model.*;

import javax.sql.DataSource;
import java.util.List;

@Repository

/**
 * DAO
 * 데이터베이스 관련 작업을 전담하는 클래스
 * 데이터베이스에 연결하여 입력, 수정, 삭제, 조회 등의 작업을 수행
 */
public class UserDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * DAO 관련 함수코드의 전반부는 크게 String ~~~Query와 Object[] ~~~~Params, jdbcTemplate 함수로 구성되어 있습니다.(보통은 동적 쿼리문이지만, 동적쿼리가 아닐 경우, Params부분은 없어도 됩니다.)
     * Query 부분은 DB에 SQL요청을 할 쿼리문을 의미하는데, 대부분의 경우 동적 쿼리(실행할 때 값이 주입되어야 하는 쿼리) 형태입니다.
     * 그래서 Query의 동적 쿼리에 입력되어야 할 값들이 필요한데 그것이 Params 부분입니다.
     * Params 부분은 클라이언트의 요청에서 제공하는 정보(~~~~Req.java에 있는 정보)로 부터 getXXX를 통해 값을 가져옵니다. ex) getEmail -> email값을 가져옵니다.
     *      Notice!
     *      get과 get의 대상은 카멜케이스로 작성됩니다. ex) item -> getItem, password -> getPassword, email -> getEmail, userIdx -> getUserIdx
     * 그 다음 GET, POST, PATCH 메소드에 따라 jabcTemplate의 적절한 함수(queryForObject, query, update)를 실행시킵니다.(DB 요청이 일어납니다.)
     *      Notice!
     *      POST, PATCH의 경우 jdbcTemplate.update
     *      GET은 대상이 하나일 경우 jdbcTemplate.queryForObject, 대상이 복수일 경우 jdbcTemplate.query 함수를 사용합니다.
     * jdbcTemplate이 실행시킬 때 Query 부분과 Params 부분은 매핑(값을 주입)시켜서 DB에 요청합니다.
     *
     * 정리하자면 < 동적 쿼리문 설정(Query) -> 주입될 값 설정(Params) -> jdbcTemplate함수(Query, Params)를 통해 Query, Params를 대응시켜 DB에 요청 > 입니다.
     *
     * DAO 관련 함수코드의 후반부는 전반부 코드를 실행시킨 후 어떤 결과값을 반환(return)할 것인지를 결정합니다.
     * 어떠한 값을 반환할 것인지 정의한 후, return 문에 전달하면 됩니다.
     * ex) return this.jdbcTemplate.query( ~~~~ ) -> ~~~~쿼리문을 통해 얻은 결과를 반환합니다.
     */

    /**
     * 참고 링크
     * https://jaehoney.tistory.com/34 -> JdbcTemplate 관련 함수
     * https://velog.io/@seculoper235/RowMapper%EC%97%90-%EB%8C%80%ED%95%B4 -> RowMapper
     */

    // 회원가입
    public int createUser(PostUserReq postUserReq) {
        String createUserQuery = "insert into User (email, password, nickname) VALUES (?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getEmail(), postUserReq.getPassword(), postUserReq.getNickname()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(createUserQuery, createUserParams);
        // email -> postUserReq.getEmail(), password -> postUserReq.getPassword(), nickname -> postUserReq.getNickname() 로 매핑한 다음 쿼리문 실행
        // 즉, DB의 User Table에 (email, password, nickname)값을 가지는 유저 데이터를 삽입(생성)한다.

        String lastInsertIdQuery = "select last_insert_id()"; // 가장 마지막에 삽입된(생성된) id값을 가져온다.
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class); // 해당 쿼리문의 결과 마지막으로 삽인된 유저의 userIdx번호를 반환한다.
    }

    // 이메일 확인
    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email; // 확인할 이메일 값
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams); // checkEmailQuery, checkEmailParams를 통해 가져온 값(int)을 반환한다. -> 쿼리문의 결과(존재하지 않음(False, 0),존재함(True, 1))를 int형(0,1)으로 반환
    }

    // 회원정보 변경 (nickname 변경)
    public int modifyUserName(PatchUserReq patchUserReq) {
        String modifyUserNameQuery = "update User set nickname = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getNickname(), patchUserReq.getUserIdx()}; // 주입될 값들(nickname, userIdx) 순

        return this.jdbcTemplate.update(modifyUserNameQuery, modifyUserNameParams); // 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)
    }


    // 로그인: 해당 email에 해당되는 user의 암호화된 비밀번호 값을 가져온다.
    public User getPwd(PostLoginReq postLoginReq) {
        String getPwdQuery = "select userIdx, password, email, nickname from User where email = ?";
        String getPwdParams = postLoginReq.getEmail(); // 주입될 email값을 클라이언트의 요청에서 가져온다.

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("userIdx"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nickname")
                ),
                getPwdParams
        ); // 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    // User 테이블에 존재하는 전체 유저들의 정보 조회
    public List<GetUserRes> getUsers() {
        String getUsersQuery = "select * from User";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("Email"),
                        rs.getString("password"))
        ); // 복수개의 회원정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보)의 결과 반환(동적쿼리가 아니므로 Parmas부분이 없음)
    }

    // 해당 nickname을 갖는 유저들의 정보 조회
    public List<GetUserRes> getUsersByNickname(String nickname) {
        String getUsersByNicknameQuery = "select * from User where nickname =?";
        String getUsersByNicknameParams = nickname;
        return this.jdbcTemplate.query(getUsersByNicknameQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUsersByNicknameParams); // 해당 닉네임을 갖는 모든 User 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    // 해당 userIdx를 갖는 유저조회
    public GetUserRes getUser(int userIdx) {
        String getUserQuery = "select * from User where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUserParams); // 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }
}