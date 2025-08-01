package egovframework.cms.member.security.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import egovframework.cms.board.service.BoardService;
import egovframework.cms.board.service.BoardVO;
import egovframework.cms.member.security.LoginVO;

@Component("boardSecurity")
public class BoardSecurity {
	
	@Autowired BoardService boardService;

    public boolean isOwner(int boardId, Authentication auth) {
        BoardVO post = boardService.getBoardDetail(boardId);
        if (post == null || auth == null || auth.getPrincipal() == null) return false;
        String loginUuid = ((LoginVO) auth.getPrincipal()).getUserUuid();
        return loginUuid != null && loginUuid.equals(post.getUserUuid());
    }

}
