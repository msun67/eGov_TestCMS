package egovframework.cms.board.web;

import egovframework.cms.board.service.BoardMasterService;
import egovframework.cms.board.service.BoardMasterVO;
//import egovframework.cms.board.service.BoardVO;

import org.springframework.security.core.Authentication;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/boardMaster")
public class BoardMasterController {
	
	 //private static final Logger logger = LoggerFactory.getLogger(BoardMasterController.class);


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
    	
    	boardMasterVO.setCreatedBy(auth.getName());
    	if (boardMasterVO.getUseyn() == 0) boardMasterVO.setUseyn(1);
    	
    	boardMasterService.createBoard(boardMasterVO);
    	
    	redirectAttributes.addFlashAttribute("message", "게시판이 생성되었습니다.");
    	
        return "redirect:/admin/boardMaster/list.do";
    }
    
    // 게시판 사용 여부 체크
    @PostMapping("/useYn.do")
    public String updateUseYn(@RequestParam String boardCode,
                              @RequestParam(defaultValue = "1") int useyn,
                              RedirectAttributes redirectAttributes) {
    	System.out.println("[DEBUG] updateUseYn called: boardCode=" + boardCode + ", useyn=" + useyn);
        boardMasterService.updateUseYn(boardCode, useyn);
        redirectAttributes.addFlashAttribute("message", "사용 여부가 업데이트되었습니다.");
        return "redirect:/admin/boardMaster/create.do";
    }

    // 게시판 목록
    @GetMapping("/list.do")
    public String list(Model model) {
        model.addAttribute("boardList", boardMasterService.getBoardList());
        return "boardMaster/list";
    }
    
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
        boardMasterService.updateBoard(boardMasterVO);
        redirectAttributes.addFlashAttribute("message", "수정이 완료되었습니다.");
        return "redirect:/admin/boardMaster/list.do";
    }
    
    //생성된 게시판 삭제
    @PostMapping("/delete.do")
    public String deleteBoard(@RequestParam String boardCode, RedirectAttributes redirectAttributes) {
        boardMasterService.deleteBoard(boardCode);
        redirectAttributes.addFlashAttribute("message", "게시판이 삭제되었습니다.");
        return "redirect:/admin/boardMaster/list.do";
    }
}
