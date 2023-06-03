package umc.crud.src.board.model;

public class Board {
    private int boardId;
    private String content;
    private String writer;

    public Board() {
    }

    public Board(int boardId, String content, String writer) {
        this.boardId = boardId;
        this.content = content;
        this.writer = writer;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

}