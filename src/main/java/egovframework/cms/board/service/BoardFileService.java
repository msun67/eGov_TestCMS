package egovframework.cms.board.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public interface BoardFileService {
	void saveFile(BoardFileVO boardfile);
	void deleteFilesByIds(List<Integer> fileIds) throws Exception;
	List<BoardFileVO> getFileListByBoardId(int boardId);
    void uploadFiles(int boardId, List<MultipartFile> files, HttpServletRequest request) throws Exception;
    BoardFileVO getFileById(int fileId) throws Exception;  
}
