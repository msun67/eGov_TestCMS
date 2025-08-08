package egovframework.cms.board.service;

import java.util.Date;

public class BoardMasterVO {
	private String boardCode;
	private String boardName;
	private String description;
	private int useyn;
	private String createdBy;
	private Date createdAt;
	// 게시판 생성시 글쓰기 권한 관련
	private String writePermitType; // CSV 저장용
	private String[] writePermitTypesArray; // 체크박스 전송용 (Spring이 자동 매핑)
	
	
	public int getUseyn() {
		return useyn;
	}
	public void setUseyn(int useyn) {
		this.useyn = useyn;
	}
	public String getBoardCode() {
		return boardCode;
	}
	public void setBoardCode(String boardCode) {
		this.boardCode = boardCode;
	}
	public String getBoardName() {
		return boardName;
	}
	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getWritePermitType() {
		return writePermitType;
	}
	public void setWritePermitType(String writePermitType) {
		this.writePermitType = writePermitType;
	}
	public String[] getWritePermitTypesArray() {
		return writePermitTypesArray;
	}
	public void setWritePermitTypesArray(String[] writePermitTypesArray) {
		this.writePermitTypesArray = writePermitTypesArray;
	}
	
}
