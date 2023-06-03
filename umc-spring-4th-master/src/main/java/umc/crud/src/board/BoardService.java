package umc.crud.src.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.crud.config.BaseException;
import umc.crud.config.BaseResponseStatus;
import umc.crud.src.board.model.*;


@Service
public class BoardService {
    @Autowired
    private BoardDao boardDao;

    @Autowired
    private BoardProvider boardProvider;

    /**
     * 글 생성
     * POST
     * @param postBoardReq
     * @return
     * @throws BaseException
     */
    @Transactional
    public PostBoardRes writeBoard(PostBoardReq postBoardReq) throws BaseException{

        try {
            int boardId = boardDao.writeBoard(postBoardReq);
            return new PostBoardRes(boardId);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /**
     * 글 내용 수정
     * PUT
     * @param putBoardReq
     * @throws BaseException
     */
    @Transactional
    public void modifyContent(PutBoardReq putBoardReq) throws BaseException {
        try {
            int result = boardDao.modifyContent(putBoardReq);
            if (result == 0) {
                throw new BaseException(BaseResponseStatus.MODIFY_FAIL_CONTENT);
            }
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /**
     * 글 삭제
     * DELETE
     * @param boardId
     */
    @Transactional
    public void deleteBoard(int boardId) {
        boardDao.deleteBoard(boardId);
    }

}