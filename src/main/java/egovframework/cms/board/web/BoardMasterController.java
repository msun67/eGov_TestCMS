package egovframework.cms.board.web;

import egovframework.cms.board.service.BoardMasterService;
import egovframework.cms.board.service.BoardMasterVO;
//import egovframework.cms.board.service.BoardVO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/boardMaster")
public class BoardMasterController {
	
    private final BoardMasterService boardMasterService;

    public BoardMasterController(BoardMasterService boardMasterService) {
        this.boardMasterService = boardMasterService;
    }

    // 게시판 생성 폼
    @GetMapping("/create.do")
    public String createForm(Model model) {
    	String nextCode = boardMasterService.getNextBoardCode();
    	model.addAttribute("nextBoardCode", nextCode);
        model.addAttribute("boardList", boardMasterService.getBoardList());
        return "boardMaster/create";
    }

    // 게시판 생성 처리
    @PostMapping("/create.do")
    public String createSubmit(@ModelAttribute BoardMasterVO boardMasterVO, Authentication auth,
            					RedirectAttributes redirectAttributes) {
    	try {
    		boardMasterVO.setCreatedBy(auth.getName());
        	
        	// ✅ useyn(사용여부) 기본처리 (0=사용안함, 1=사용함)
        	if (boardMasterVO.getUseyn() == 0) boardMasterVO.setUseyn(1);
        	
        	// ✅ 게시판생성하면서 글쓰기 권한 처리(0=관리자, 1=사용자, 2=조직원 / 0무조건포함)
            String[] checked = boardMasterVO.getWritePermitTypesArray();
            Set<String> permitSet = new HashSet<>();
            permitSet.add("0"); // 관리자
            if (checked != null) permitSet.addAll(Arrays.asList(checked));
            boardMasterVO.setWritePermitType(String.join(",", permitSet));	
        	
        	boardMasterService.createBoard(boardMasterVO);    	
        	redirectAttributes.addFlashAttribute("okMessage", "✅ 게시판이 생성되었습니다.");
        	
            return "redirect:/admin/boardMaster/create.do";
            
    	}catch(Exception e) {
    		e.printStackTrace(); // 로그 출력
            redirectAttributes.addFlashAttribute("errorMessage", "⚠️ 수정에 실패했습니다. 관리자에게 문의하세요.");
            return "redirect:/admin/boardMaster/edit.do?boardCode=" + boardMasterVO.getBoardCode();
    	}
    }
    
    // 게시판 사용 여부 체크
    @PostMapping("/useYn.do")
    public String updateUseYn(@RequestParam String boardCode,
                              @RequestParam(defaultValue = "1") int useyn,
                              RedirectAttributes redirectAttributes) {    	
    	try {
    		System.out.println("[DEBUG] updateUseYn called: boardCode=" + boardCode + ", useyn=" + useyn);
            boardMasterService.updateUseYn(boardCode, useyn);
            redirectAttributes.addFlashAttribute("okMessage", "✅ 사용 여부가 업데이트되었습니다.");
            return "redirect:/admin/boardMaster/create.do";
    		
    	}catch(Exception e) {
    		e.printStackTrace(); // 로그 출력
            redirectAttributes.addFlashAttribute("errorMessage", "⚠️ 수정에 실패했습니다. 관리자에게 문의하세요.");
            return "redirect:/admin/boardMaster/edit.do";
    	}
    }

    // 게시판 목록
	/*
	 * @GetMapping("/list.do") public String list(Model model) {
	 * model.addAttribute("boardList", boardMasterService.getBoardList()); return
	 * "boardMaster/list"; }
	 */
    
    // 생성된 게시판 수정 페이지로 이동
    @GetMapping("/edit.do")
    public String editForm(@RequestParam String boardCode, Model model) {
        BoardMasterVO board = boardMasterService.getBoardByCode(boardCode);
       
        model.addAttribute("boardMasterVO", board);
        return "boardMaster/edit";
    }
    
    // 생성된 게시판 수정 처리
    @PostMapping("/update.do")
    public String updateSubmit(@ModelAttribute BoardMasterVO boardMasterVO,
            					RedirectAttributes redirectAttributes) {
    	
    	try {
    		// ✅ 체크박스가 모두 선택되지 않았을 경우 → "0" (관리자만 허용)
            if (boardMasterVO.getWritePermitTypesArray() == null || boardMasterVO.getWritePermitTypesArray().length == 0) {
                boardMasterVO.setWritePermitType("0"); // 관리자만 허용
            } else {
                // ✅ CSV 형태로 변환
                boardMasterVO.setWritePermitType(String.join(",", boardMasterVO.getWritePermitTypesArray()));
            }
        	
            boardMasterService.updateBoard(boardMasterVO);    
            redirectAttributes.addFlashAttribute("okMessage", "✅ 수정이 완료되었습니다.");
            return "redirect:/admin/boardMaster/create.do";
    		
    	}catch(Exception e) {
    		e.printStackTrace(); // 로그 출력
            redirectAttributes.addFlashAttribute("errorMessage", "⚠️ 수정에 실패했습니다. 관리자에게 문의하세요.");
            return "redirect:/admin/boardMaster/edit.do?boardCode=" + boardMasterVO.getBoardCode();
    	}
    }
    
    //생성된 게시판 삭제
    @PostMapping("/delete.do")
    public String deleteBoard(@RequestParam String boardCode, RedirectAttributes redirectAttributes) {
    	
    	try {
    		boardMasterService.removeBoardWithPosts(boardCode);
            redirectAttributes.addFlashAttribute("okMessage", "✅ 게시판이 삭제되었습니다.");
            return "redirect:/admin/boardMaster/create.do";
            
    	}catch(Exception e) {
    		e.printStackTrace(); // 로그 출력
            redirectAttributes.addFlashAttribute("errorMessage", "⚠️ 수정에 실패했습니다. 관리자에게 문의하세요.");
            return "redirect:/admin/boardMaster/edit.do";    		
    	}
    }
}
