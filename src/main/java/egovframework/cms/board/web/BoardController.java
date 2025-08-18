package egovframework.cms.board.web;

import java.io.File;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
import egovframework.cms.member.security.LoginVO;

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

	@GetMapping("/board.do")
	public String boardList(@ModelAttribute("searchVO") SearchVO searchVO,
	                        @RequestParam(value = "boardCode", required = false) String boardCode,
	                        Model model, Authentication auth) throws Exception {

		// ✅ 게시판 생성시 글쓰기 권한 가져오기 위해서 사용.
	    BoardMasterVO board = null;
	    boolean canWrite = false;

	    if (boardCode != null && !boardCode.isEmpty()) {
	        board = boardMasterService.getBoardInfo(boardCode);
	        model.addAttribute("board", board);
	        System.out.println("board: " + board);

	        // ✅ 글쓰기 권한 체크
	        if (auth != null && auth.isAuthenticated()) {
	            LoginVO loginUser = (LoginVO) auth.getPrincipal();
	            int userType = loginUser.getUserType();
	            System.out.println("현재 로그인 사용자 타입: " + userType);

	            if (userType == 0) {
	                canWrite = true;
	                System.out.println("글쓰기 가능 여부: " + canWrite);
	            } else if (board != null && board.getWritePermitType() != null) {
	            	System.out.println("board.getWritePermitType(): " + board.getWritePermitType());
	                List<Integer> permitted = Arrays.stream(board.getWritePermitType().split(","))
	                        .map(String::trim)
	                        .map(Integer::parseInt)
	                        .collect(Collectors.toList());
	                		System.out.println("게시판 허용 타입: " + permitted);

	                if (permitted.contains(userType)) {
	                    canWrite = true;
	                    System.out.println("글쓰기 가능 여부: " + canWrite);
	                }
	            }
	        }
	    }

	    // boardCode를 SearchVO에 설정
	    searchVO.setBoardCode(boardCode);
	    if (searchVO.getPage() < 1) searchVO.setPage(1);

	    int totalCnt = boardService.getBoardListCnt(searchVO);
	    int pageSize = searchVO.getSize();
	    int totalPages = (int) Math.ceil((double) totalCnt / pageSize);
	    if (searchVO.getPage() > totalPages && totalPages > 0) {
	        searchVO.setPage(totalPages);
	    }

	    List<BoardVO> boardList = boardService.getBoardList(searchVO);
	    List<BoardMasterVO> boardMasterList = boardMasterService.getBoardMasterList();

	    model.addAttribute("boardList", boardList);
	    model.addAttribute("boardCode", boardCode);
	    model.addAttribute("boardMasterList", boardMasterList);
	    model.addAttribute("totalCnt", totalCnt);
	    model.addAttribute("page", searchVO.getPage());
	    model.addAttribute("pageSize", pageSize);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("canWrite", canWrite);

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
	@PreAuthorize("(#boardCode != 'notice') or hasRole('ROLE_ADMIN')")
	// 글쓰기 화면에서 등록 버튼 클릭 후
	@PostMapping("/write.do")
	public String writeSubmit(BoardVO boardVO,
								@RequestParam("boardCode") String boardCode,
		                        @RequestParam("uploadFiles") MultipartFile[] files,
		                        HttpServletRequest request, RedirectAttributes redirect) throws Exception {
		
	    System.out.println("====[ 디버깅: Request 파라미터 ]====");
	    request.getParameterMap().forEach((k, v) -> System.out.println("Param: " + k + " = " + Arrays.toString(v)));

	    System.out.println("====[ 디버깅: Multipart Part 목록 ]====");
	    for (Part part : request.getParts()) {
	        System.out.println("📦 Part name = " + part.getName() + ", size = " + part.getSize());
	    }
		
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
	                //String extension = originalName.substring(originalName.lastIndexOf("."));
	                String extension = "";
	                if (originalName != null && originalName.contains(".")) {
	                    extension = originalName.substring(originalName.lastIndexOf("."));
	                }
	                // 날짜 생성: _yyMMdd
	                String dateSuffix = new SimpleDateFormat("_yyMMdd").format(new Date());
	                String uuid = UUID.randomUUID().toString();
	                String saveName = uuid + dateSuffix + extension;

	                File dest = new File(uploadDir, saveName);
	                file.transferTo(dest); // 파일 저장

	                // DB에 저장할 객체 생성
	                BoardFileVO boardFile = new BoardFileVO();
	                boardFile.setBoardId(boardId);
	                boardFile.setOriginalName(originalName);
	                boardFile.setSaveName(saveName);//폴더에 저장될 파일명 uuid+날짜+확장자
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
		//return "board/write";
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
	    	// null, "", 공백 제거
	    	List<Integer> cleanIds = deleteFileIds.stream()
	    		.filter(Objects::nonNull)
	    		.map(String::valueOf)
	    		.filter(id -> !id.isBlank())
	    		.map(Integer::parseInt)
	    		.collect(Collectors.toList());

	    	if (!cleanIds.isEmpty()) {
	    		System.out.println("🧨 실제 삭제할 파일 IDs: " + cleanIds);
	    		boardFileService.deleteFilesByIds(cleanIds);
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
	
	//내가 쓴 글
	@GetMapping("/posts.do")
	@PreAuthorize("isAuthenticated()")
	public String myPosts(@ModelAttribute("searchVO") SearchVO searchVO,
	                      @RequestParam(value = "boardCode", required = false) String boardCode,
	                      Model model, Authentication auth) throws Exception {

	    LoginVO login = (LoginVO) auth.getPrincipal();
	    String authorUuid = login.getUserUuid(); // 또는 getUserId()

	    searchVO.setBoardCode(boardCode);              // 특정 게시판만 보고 싶으면 유지, 전체면 null/빈문자
	    if (searchVO.getPage() < 1) searchVO.setPage(1);

	    int totalCnt = boardService.getBoardListCntByAuthor(searchVO, authorUuid);
	    int pageSize = searchVO.getSize();
	    int totalPages = (int)Math.ceil((double)totalCnt / pageSize);
	    if (searchVO.getPage() > totalPages && totalPages > 0) {
	        searchVO.setPage(totalPages);
	    }

	    List<BoardVO> boardList = boardService.getBoardListByAuthor(searchVO, authorUuid);
	    List<BoardMasterVO> boardMasterList = boardMasterService.getBoardMasterList();

	    model.addAttribute("boardList", boardList);
	    model.addAttribute("boardCode", boardCode);
	    model.addAttribute("boardMasterList", boardMasterList);
	    model.addAttribute("totalCnt", totalCnt);
	    model.addAttribute("page", searchVO.getPage());
	    model.addAttribute("pageSize", pageSize);
	    model.addAttribute("totalPages", totalPages);

	    model.addAttribute("myPosts", true);  // JSP에서 제목/버튼 제어용
	    model.addAttribute("canWrite", false);// 내 글 모아보기에서는 글쓰기 숨김(선택)

	    return "board/list";
	}
}
