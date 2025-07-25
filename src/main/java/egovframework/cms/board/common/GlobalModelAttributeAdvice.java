package egovframework.cms.board.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import egovframework.cms.board.service.BoardMasterService;
import egovframework.cms.board.service.BoardMasterVO;

@ControllerAdvice
public class GlobalModelAttributeAdvice {
	 @Autowired
	 private BoardMasterService boardMasterService;
	 
	// 모든 컨트롤러의 모델에 boardMasterList를 자동으로 포함시킴 (leftmenu 사용)
	 @ModelAttribute("boardMasterList")
	    public List<BoardMasterVO> populateBoardMasterList() {
	        return boardMasterService.getBoardMasterList();
	    }
}
