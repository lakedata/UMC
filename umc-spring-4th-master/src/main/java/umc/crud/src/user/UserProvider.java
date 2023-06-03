package umc.crud.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.crud.config.BaseException;
import umc.crud.config.secret.Secret;
import umc.crud.src.user.model.GetUserRes;
import umc.crud.src.user.model.PostLoginReq;
import umc.crud.src.user.model.PostLoginRes;
import umc.crud.src.user.model.User;
import umc.crud.utils.AES128;
import umc.crud.utils.JwtService;

import java.util.List;

import static umc.crud.config.BaseResponseStatus.*;

@Service
/**
 * Provider
 * Controller에 의해 호출되어 실제 비즈니스 로직과 트랜잭션을 처리: Read의 비즈니스 로직 처리
 * 요청한 작업을 처리하는 관정을 하나의 작업으로 묶음
 * dao를 호출하여 DB CRUD를 처리 후 Controller로 반환
 */
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    /**
     * 로그인(password 검사)
     * @param postLoginReq
     * @return PostLoginRes
     * @throws BaseException
     */
    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException {
        User user = userDao.getPwd(postLoginReq);
        String password;
        try {
            password = new AES128(Secret.USER_PASSWORD_KEY).decrypt(user.getPassword()); // 복호화
            // 회원가입할 때 비밀번호가 암호화되어 저장되었기 떄문에 로그인 시애도 암호화된 값끼리 비교
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if (postLoginReq.getPassword().equals(password)) { // 비밀번호가 일치하면 userIdx를 가져온다.
            int userIdx = userDao.getPwd(postLoginReq).getUserIdx();
            String jwt = jwtService.createJwt(userIdx); // 가져온 userIdx로 jwt 생성
            return new PostLoginRes(userIdx,jwt);
        } else { // 비밀번호가 다르면 에러메세지 출력
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    /**
     * 해당 이메일이 이미 User Table에 존재하는지 확인
     */
    public int checkEmail(String email) throws BaseException {
        try {
            return userDao.checkEmail(email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * GET
     * User들 정보 조회
     * @return getUserRes
     * @throws BaseException
     */
    public List<GetUserRes> getUsers() throws BaseException {
        try {
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * GET
     * 해당 nickname을 갖는 User들의 정보 조회
     * @param nickname
     * @return getUserRes
     * @throws BaseException
     */
    public List<GetUserRes> getUsersByNickname(String nickname) throws BaseException {
        try {
            List<GetUserRes> getUsersRes = userDao.getUsersByNickname(nickname);
            return getUsersRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * GET
     * 해당 userIdx를 갖는 User의 정보 조회
     * @param userIdx
     * @return getUserRes
     * @throws BaseException
     */
    public GetUserRes getUser(int userIdx) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(userIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}