package egovframework.cms.board.service.impl;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.cms.board.service.BoardFileService;
import egovframework.cms.board.service.BoardFileVO;

import egovframework.cms.config.UploadConstants;

@Service("boardFileService")
public class BoardFileServiceImpl implements BoardFileService {
	
	private final BoardDAO boardDAO;
	
	 public BoardFileServiceImpl(BoardDAO boardDAO) {
	        this.boardDAO = boardDAO;
	    }

	 
	//첨부파일등록
	@Override
	public void saveFile(BoardFileVO boardfile) {
		boardDAO.insertFile(boardfile);
	}
	
	// 기존 첨부파일 교체 시 삭제할 대상으로 입력
	@Override
    public void deleteFilesByIds(List<Integer> fileIds) throws Exception {
		if (fileIds == null || fileIds.isEmpty()) return;
		
		// 삭제 대상 파일 정보 조회(db)
        List<BoardFileVO> files = boardDAO.getFilesByIds(fileIds);
        
        // 물리적 파일 삭제
        if (files != null) {
        	for (BoardFileVO fileVO : files) {
        		if (fileVO != null && fileVO.getFilePath() != null && fileVO.getSaveName() != null) {
                    File physicalFile = new File(fileVO.getFilePath(), fileVO.getSaveName());
                    if (physicalFile.exists()) {
                        boolean deleted = physicalFile.delete();
                        System.out.println("파일 삭제됨? : " + deleted + ", 경로: " + physicalFile.getAbsolutePath());
                    } else {
                        System.out.println("파일 존재하지 않음 : " + physicalFile.getAbsolutePath());
                    }
                }
            }
        }
        // db에서 파일 메타데이터 삭제
        boardDAO.deleteFilesByIds(fileIds);
    }
	
	// 게시글 상세보기시 첨부파일 목록 조회
	@Override
	public List<BoardFileVO> getFileListByBoardId(int boardId) {
		return boardDAO.getFileListByBoardId(boardId);
	}
	
	
	
	
	@Override
	public BoardFileVO getFileById(int fileId) {
	    return boardDAO.getFileById(fileId);
	}
	
	

	@Override
	public void uploadFiles(int boardId, List<MultipartFile> files, HttpServletRequest request) throws Exception {
	    String uploadPath = UploadConstants.UPLOAD_PATH;

	    File uploadDir = new File(uploadPath);
	    if (!uploadDir.exists()) {
	        uploadDir.mkdirs();
	    }

	    for (MultipartFile multipartFile : files) {
	        if (!multipartFile.isEmpty()) {
	            String originalName = multipartFile.getOriginalFilename();
	            String saveName = UUID.randomUUID().toString() + "_" + originalName;
	            String fileType = multipartFile.getContentType();
	            long fileSize = multipartFile.getSize();

	            File dest = new File(uploadPath, saveName);
	            multipartFile.transferTo(dest); // 실제 파일 저장

	            // DB 저장용 객체 구성
	            BoardFileVO fileVO = new BoardFileVO();
	            fileVO.setBoardId(boardId);
	            fileVO.setOriginalName(originalName);
	            fileVO.setSaveName(saveName);
	            fileVO.setFilePath(uploadPath); // 디렉토리 경로만 저장
	            fileVO.setFileSize(fileSize);
	            fileVO.setFileType(fileType);

	            boardDAO.insertFile(fileVO);
	        }
	    }
	}

}
