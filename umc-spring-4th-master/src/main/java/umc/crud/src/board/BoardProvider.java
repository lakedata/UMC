package umc.crud.src.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.crud.src.board.model.GetBoardRes;

import java.util.List;

@Service
public class BoardProvider {

    @Autowired
    private BoardDao boardDao;

    /**
     * 모든 글 조회
     * @return
     */
    public List<GetBoardRes> getAllBoards() {

        return boardDao.getAllBoards();
    }

    /**
     * 특정 글 조회
     * @param writer
     * @return
     */
    public List<GetBoardRes> getBoardByWriter(String writer) {

        return boardDao.getBoardByWriter(writer);
    }

    public List<GetBoardRes> getBoardByBoardId(int boardId) {
        return boardDao.getBoardByBoardId(boardId);
    }
}
