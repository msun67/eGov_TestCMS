package egovframework.cms.board.service;

public class BoardFileVO {
	private int fileId;
    private int boardId;
    private String originalName;
    private String saveName;
    private String filePath;
    private long fileSize;
    private String fileType;
    private boolean isDeleted;
    private String uploadedAt;
    private String deletedAt;
	public int getFileId() {
		return fileId;
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	public String getSaveName() {
		return saveName;
	}
	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getUploadedAt() {
		return uploadedAt;
	}
	public void setUploadedAt(String uploadedAt) {
		this.uploadedAt = uploadedAt;
	}
	public String getDeletedAt() {
		return deletedAt;
	}
	public void setDeletedAt(String deletedAt) {
		this.deletedAt = deletedAt;
	}
	
	@Override
	public String toString() {
	    return "BoardFileVO{" +
	            "fileId=" + fileId +
	            ", boardId=" + boardId +
	            ", originalName='" + originalName + '\'' +
	            ", saveName='" + saveName + '\'' +
	            ", filePath='" + filePath + '\'' +
	            ", fileSize=" + fileSize +
	            ", fileType='" + fileType + '\'' +
	            ", isDeleted=" + isDeleted +
	            ", uploadedAt='" + uploadedAt + '\'' +
	            ", deletedAt='" + deletedAt + '\'' +
	            '}';
	}
}
