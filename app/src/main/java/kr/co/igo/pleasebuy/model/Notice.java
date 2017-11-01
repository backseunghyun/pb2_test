package kr.co.igo.pleasebuy.model;


public class Notice {
    private int boardId;
    private String title;
    private Long regDate;
    private String contents;
    private String imgUrl;
    private String type;
    private String fromDate;
    private String toDate;

    public String getTitle() {return title;}
    public String getContents() {return contents;}
    public String getImgUrl() { return imgUrl; }


    public void setTitle(String title) {this.title = title; }
    public void setContents(String contents) {this.contents = contents; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public Long getRegDate() {
        return regDate;
    }

    public void setRegDate(Long regDate) {
        this.regDate = regDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }
}
