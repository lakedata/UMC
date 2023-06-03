package umc.crud.src.board.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수를 받는 생성자 생성
@NoArgsConstructor // 해당 클래스의 파라미터가 없는 생성자를 생성
public class PutBoardReq {
    private int boardId;
    private String content;
    private String writer;

    public PutBoardReq(int boardId, String content) {
        this.boardId = boardId;
        this.content = content;
    }


}