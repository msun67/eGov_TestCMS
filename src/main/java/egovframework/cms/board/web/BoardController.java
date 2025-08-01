package egovframework.cms.board.web;

import java.io.File;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.cms.board.service.BoardFileService;
import egovframework.cms.board.service.BoardFileVO;
import egovframework.cms.board.service.BoardMasterService;
import egovframework.cms.board.service.BoardMasterVO;
import egovframework.cms.board.service.BoardService;
import egovframework.cms.board.service.BoardVO;
import egovframework.cms.board.service.SearchVO;

import egovframework.cms.config.UploadConstants;

@Controller
public class BoardController {
	private final BoardService boardService;
	private final BoardMasterService boardMasterService;
	private final BoardFileService boardFileService;

	public BoardController(BoardService boardService, BoardMasterService boardMasterService, BoardFileService boardFileService) {
		this.boardService = boardService;
		this.boardMasterService = boardMasterService;
		this.boardFileService = boardFileService;
	}

	// 게시글 목록
	@GetMapping("/board.do")
	public String boardList(@ModelAttribute("searchVO") SearchVO searchVO,
	                        @RequestParam(value = "boardCode", required = false) String boardCode,
	                        Model model) throws Exception {

	    if (searchVO.getPage() < 1) searchVO.setPage(1); // 기본값

	    // boardCode를 SearchVO에 설정
	    searchVO.setBoardCode(boardCode);

	    // 전체 게시글 수 조회
	    int totalCnt = boardService.getBoardListCnt(searchVO);

	    // 총 페이지 수 계산
	    int pageSize = searchVO.getSize();
	    int totalPages = (int) Math.ceil((double) totalCnt / pageSize);

	    // 현재 페이지가 총 페이지보다 크면 보정
	    if (searchVO.getPage() > totalPages && totalPages > 0) {
	        searchVO.setPage(totalPages);
	    }

	    // 게시글 리스트 조회
	    List<BoardVO> boardList = boardService.getBoardList(searchVO);

	    // 게시판 목록 조회 (좌측 네비게이션용)
	    List<BoardMasterVO> boardMasterList = boardMasterService.getBoardMasterList();

	    // 모델에 값 전달
	    model.addAttribute("boardList", boardList);
	    model.addAttribute("boardCode", boardCode);
	    model.addAttribute("boardMasterList", boardMasterList);
	    model.addAttribute("totalCnt", totalCnt);
	    model.addAttribute("page", searchVO.getPage());
	    model.addAttribute("pageSize", pageSize);
	    model.addAttribute("totalPages", totalPages); 

	    return "board/list";
	}

	// 게시글 상세보기
	@GetMapping("/detail.do")
	public String boardDetail(@RequestParam("boardId") int boardId,
								@RequestParam("boardCode") String boardCode,
								Model model) throws Exception {
		// 조회수 증가
		boardService.updateViewCount(boardId);

		// 게시글 상세조회
		BoardVO board = boardService.getBoardDetail(boardId);
		
		// 첨부파일 목록 조회
	    List<BoardFileVO> fileList = boardFileService.getFileListByBoardId(boardId);
	    
		// 이전글, 다음글
		BoardVO prevPost = boardService.getPrevPost(board.getCreatedAt(), boardCode);
	    BoardVO nextPost = boardService.getNextPost(board.getCreatedAt(), boardCode);
	    
	    // ✅ 이스케이프 복호화 처리 추가
	    board.setBoardContent(StringEscapeUtils.unescapeHtml4(board.getBoardContent()));
		
		model.addAttribute("board", board);
		model.addAttribute("fileList", fileList); //첨부파일 목록 전달
		//System.out.println("컨트롤러에서 fileList : " + fileList);
		model.addAttribute("prevPost", prevPost);
		model.addAttribute("nextPost", nextPost);
		model.addAttribute("boardCode", boardCode); // JSP에서 다시 목록 등으로 돌아갈 때 사용
		return "board/detail"; // 상세보기 페이지로 이동
	}

	//boardCode가 notice면 관리자만 통과하도록 표현식 평가
	@PreAuthorize("(#boardCode != 'notice') or hasRole('ROLE_ADMIN')")
	// 글쓰기 페이지로 이동
	@GetMapping("/write.do")
	public String writeForm(@RequestParam("boardCode") String boardCode, Model model) {
		
		List<BoardMasterVO> boardMasterList = boardMasterService.getBoardMasterList();
		
		 model.addAttribute("boardMasterList", boardMasterList);
		 model.addAttribute("boardCode", boardCode); // 현재 선택된 boardCode 전달
		
		return "board/write";
	}

	//반환값에 대해 검증
	@PreAuthorize("(#form.boardCode != 'notice') or hasRole('ROLE_ADMIN')")
	// 글쓰기 화면에서 등록 버튼 클릭 후
	@PostMapping("/write.do")
	public String writeSubmit(BoardVO boardVO,
								@RequestParam("boardCode") String boardCode,
		                        @RequestParam("uploadFiles") MultipartFile[] files,
								RedirectAttributes redirect) {
		// 1. 게시글 저장
		boardService.insertBoard(boardVO);
		int boardId = boardVO.getBoardId(); // 자동으로 생성된 ID 값 가져옴.
		
		// 2. 파일 저장 처리
		String uploadDir = UploadConstants.UPLOAD_PATH;
	    File dir = new File(uploadDir);
	    if (!dir.exists()) {
	        dir.mkdirs();
	    }

	    for (MultipartFile file : files) {
	        if (!file.isEmpty()) {
	            try {
	                String originalName = file.getOriginalFilename();
	                String extension = originalName.substring(originalName.lastIndexOf("."));
	                String uuid = UUID.randomUUID().toString();
	                String saveName = uuid + extension;

	                File dest = new File(uploadDir, saveName);
	                file.transferTo(dest); // 파일 저장

	                // DB에 저장할 객체 생성
	                BoardFileVO boardFile = new BoardFileVO();
	                boardFile.setBoardId(boardId);
	                boardFile.setOriginalName(originalName);
	                boardFile.setSaveName(saveName);//파일명
	                boardFile.setFilePath(uploadDir);//경로만 저장
	                boardFile.setFileSize((int) file.getSize());
	                boardFile.setFileType(file.getContentType());

	                boardFileService.saveFile(boardFile); // DB에 저장

	            } catch (IOException e) {
	                e.printStackTrace();
	                redirect.addFlashAttribute("errorMessage", "❌ 파일 업로드 중 오류 발생");
	            }
	        }
	    }
		 redirect.addFlashAttribute("okMessage", "✅ 등록이 완료되었습니다.");		 
		return "redirect:/board.do?boardCode=" + boardCode;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or @boardSecurity.isOwner(#boardId, authentication)")
	// 글 수정 페이지로 이동
	@GetMapping("/edit.do")
	public String updateForm(@RequestParam("boardId") int boardId,
								@RequestParam("boardCode") String boardCode,Model model) {
		BoardVO board = boardService.getBoardDetail(boardId);
		
		 // 기존 첨부파일 목록
	    List<BoardFileVO> fileList = boardFileService.getFileListByBoardId(boardId);
		
		board.setBoardCode(boardCode); //드롭박스 selected값 고정	
		
		model.addAttribute("board", board);
		model.addAttribute("boardCode", boardCode);
		
		// board_master 목록 조회
	    List<BoardMasterVO> boardMasterList = boardMasterService.getBoardMasterList();
	    model.addAttribute("boardMasterList", boardMasterList);
	    model.addAttribute("fileList", fileList); // 기존 첨부파일 전달
	    
		return "board/edit"; //글 수정 페이지로 이동
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or @boardSecurity.isOwner(#boardVO.boardId, authentication)")
	// 글 수정 화면에서 수정 처리
	@PostMapping("/update.do")
	public String updateSubmit(BoardVO boardVO,
								@RequestParam("boardCode") String boardCode,
								@RequestParam(value = "uploadFiles", required = false) List<MultipartFile> uploadFiles,
		                        @RequestParam(value = "deleteFileIds", required = false) List<Integer> deleteFileIds,
		                        HttpServletRequest request,
           						RedirectAttributes redirect) throws Exception {
		// 게시글 정보 업데이트
		boardService.updateBoard(boardVO);
		
		 // 1. 삭제된 첨부파일 처리
	    if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
	        boardFileService.deleteFilesByIds(deleteFileIds);
	        if (deleteFileIds != null) {
	            System.out.println("삭제 대상 파일 ID 목록: " + deleteFileIds); // 디버깅용
	        }
	    }

	    // 2. 새로 업로드된 파일 저장
	    if (uploadFiles != null && !uploadFiles.isEmpty()) {
	        boardFileService.uploadFiles(boardVO.getBoardId(), uploadFiles, request);
	    }
	    
		redirect.addFlashAttribute("okMessage", "✅ 수정이 완료되었습니다.");		
		return "redirect:/board.do?boardCode=" + boardCode;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or @boardSecurity.isOwner(#boardId, authentication)")
	// 게시글 삭제
	@PostMapping("/delete.do")
	public String deleteBoard(@RequestParam("boardId") int boardId,
								@RequestParam("boardCode") String boardCode,RedirectAttributes redirect) {
	    BoardVO board = boardService.getBoardDetail(boardId); // 상세 조회
	    if (board != null) {
	    	try {
	    		boardService.deleteBoard(board); // soft delete + archive
		        redirect.addFlashAttribute("okMessage", "✅ 삭제가 완료되었습니다.");
	    	}catch (Exception e) {
	    		e.printStackTrace();
	            redirect.addFlashAttribute("warningMessage", "❌ 삭제 중 오류가 발생했습니다.");
	    	}
	    }else {
	    	redirect.addFlashAttribute("errorMessage", "❌ 해당 게시글을 찾을 수 없습니다.");
	    }
	    return "redirect:/board.do?boardCode=" + boardCode;
	}
}
