package egovframework.cms.board.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public interface BoardFileService {
	void saveFile(BoardFileVO boardfile);
	List<BoardFileVO> getFileListByBoardId(int boardId);
	BoardFileVO getFileById(int fileId);
	void deleteFilesByIds(List<Integer> fileIds) throws Exception;
    void uploadFiles(int boardId, List<MultipartFile> files, HttpServletRequest request) throws Exception;
}
