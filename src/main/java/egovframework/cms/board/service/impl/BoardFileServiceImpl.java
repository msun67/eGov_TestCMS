package egovframework.cms.board.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
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
		System.out.println("🧨 deleteFilesByIds() 호출됨");
		if (fileIds == null || fileIds.isEmpty()) {
			System.out.println("⚠️ 삭제할 파일 ID가 없음 (null or empty)");
			return;
		} 
		
		// 삭제 대상 파일 정보 조회(db)
        List<BoardFileVO> files = boardDAO.findFilesByIds(fileIds);
        System.out.println("📁 삭제 대상 파일 개수: " + (files != null ? files.size() : "null"));
        
        // 물리적 파일 삭제
        for (BoardFileVO fileVO : files) {
        	System.out.println("🧾 fileVO 전체 정보: " + fileVO);

        	if (fileVO != null && fileVO.getFilePath() != null && fileVO.getSaveName() != null) {
        		File physicalFile = new File(fileVO.getFilePath(), fileVO.getSaveName());
        		System.out.println("🔍 삭제 대상 경로: " + physicalFile.getAbsolutePath());

        		if (physicalFile.exists()) {
        			boolean deleted = physicalFile.delete();
        			System.out.println("🧹 파일 삭제 결과: " + deleted);
        		} else {
        			System.out.println("❌ 파일 존재하지 않음: " + physicalFile.getAbsolutePath());
        		}
        	} else {
        		System.out.println("⚠️ fileVO가 null이거나 필수 필드(filePath/saveName)가 null입니다.");
        	}
        }    	
    	// 최종적으로 DB 메타데이터 삭제
    	boardDAO.deleteFilesByIds(fileIds);   
    }
	
	// 게시글 상세보기시 첨부파일 목록 조회
	@Override
	public List<BoardFileVO> getFileListByBoardId(int boardId) {
		return boardDAO.findFilesByPostId(boardId);
	}
	
	//게시글 수정시 새로 업로드 된 첨부파일 저장
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
	            String extension = "";
	            if (originalName != null && originalName.contains(".")) {
                    extension = originalName.substring(originalName.lastIndexOf("."));
                }
	            // 날짜 생성: _yyMMdd
                String dateSuffix = new SimpleDateFormat("_yyMMdd").format(new Date());
                String uuid = UUID.randomUUID().toString();
                String saveName = uuid + dateSuffix + extension;
	            String fileType = multipartFile.getContentType();
	            long fileSize = multipartFile.getSize();

	            File dest = new File(uploadPath, saveName);
	            multipartFile.transferTo(dest); // 실제 파일 저장

	            // DB 저장용 객체 구성
	            BoardFileVO fileVO = new BoardFileVO();
	            fileVO.setBoardId(boardId);
	            fileVO.setOriginalName(originalName);
	            fileVO.setSaveName(saveName);//폴더에 저장될 파일명 uuid+날짜+확장자
	            fileVO.setFilePath(uploadPath); // 디렉토리 경로만 저장
	            fileVO.setFileSize(fileSize);
	            fileVO.setFileType(fileType);

	            boardDAO.insertFile(fileVO);
	        }
	    }
	}
	
	// 첨부파일 다운로드
	@Override
	public BoardFileVO getFileById(int fileId) throws Exception {
	    return boardDAO.getFileById(fileId);
	}
}
