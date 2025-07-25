package egovframework.cms.board.service;

public class SearchVO {
	// 검색 조건 (제목/내용/전체)
	private String condition;
	// 검색어
	private String keyword;
	// 현재 페이지 번호
    private int page = 1;
    // 페이지당 게시글 수
    private int size = 10;  
    // LIMIT 시작 인덱스 (MySQL용)
    public int getOffset() {
        return (page - 1) * size;
    }
    // 게시판 목록 조회용
    private String boardCode;    
  
	public String getBoardCode() {
		return boardCode;
	}
	public void setBoardCode(String boardCode) {
		this.boardCode = boardCode;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}
