package egovframework.cms.member.vo;

public class MemberSearchVO {
	
	// 회원목록 검색용
	private String condition; // id, name, mobile, all
	private String keyword;
	private int page = 1;
	private int size = 10;
	public int getOffset(){ return (page - 1) * size; }

	private Integer userType; // 0/1/2
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

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}
}
