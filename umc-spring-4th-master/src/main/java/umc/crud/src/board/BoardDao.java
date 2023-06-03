package umc.crud.src.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import umc.crud.src.board.model.*;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BoardDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Board 생성
    public int writeBoard(PostBoardReq postBoardReq) {
        String writeBoardQuery = "insert into Board(boardId, content, writer) VALUES (?, ?, ?)";
        Object[] writeBoardParams = new Object[]{postBoardReq.getBoardId(), postBoardReq.getContent(), postBoardReq.getWriter()};
        this.jdbcTemplate.update(writeBoardQuery, writeBoardParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    // 모든 글 조회
    public List<GetBoardRes> getAllBoards() {
        String getBoardsQuery = "select * from Board";

        return this.jdbcTemplate.query(getBoardsQuery,
                (rs, rowNum) -> new GetBoardRes(
                        rs.getInt("boardId"),
                        rs.getString("content"),
                        rs.getString("writer"))
        );
    }

    // writer로 특정 글 조회
    public List<GetBoardRes> getBoardByWriter(String writer) {
        String getBoardByWriterQuery = "select * from Board where writer = ?";
        String getBoardByWriterParams = writer;

        return this.jdbcTemplate.query(getBoardByWriterQuery,
                (rs, rowNum) -> new GetBoardRes(
                        rs.getInt("boardId"),
                        rs.getString("content"),
                        rs.getString("writer")),
                getBoardByWriterParams);
    }

    // boardId로 특정 글 조회
    public List<GetBoardRes> getBoardByBoardId(int boardId) {
        String getBoardByBoardIdQuery = "select * from Board where boardId = ?";
        int getBoardByBoardIdParams = boardId;

        return this.jdbcTemplate.query(getBoardByBoardIdQuery,
                (rs, rowNum) -> new GetBoardRes(
                        rs.getInt("boardId"),
                        rs.getString("content"),
                        rs.getString("writer")),
                getBoardByBoardIdParams);
    }

    // 글 수정
    public int modifyContent(PutBoardReq putBoardReq) {
        String modifyContentQuery = "update Board set content = ? where writer ?";
        Object[] modifyContentParams = new Object[]{putBoardReq.getContent(), putBoardReq.getWriter()};

        return this.jdbcTemplate.update(modifyContentQuery, modifyContentParams);
    }


    // 글 삭제
    public int deleteBoard(int boardId) {
        String deleteBoardQuery = "delete from Board where boardId = ?";
        int deleteBoardParams = boardId;

        return this.jdbcTemplate.update(deleteBoardQuery, deleteBoardParams);
    }

}