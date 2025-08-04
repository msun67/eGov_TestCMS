package egovframework.cms.member.security.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import egovframework.cms.board.service.BoardService;
import egovframework.cms.board.service.BoardVO;
import egovframework.cms.member.security.LoginVO;

// boardId의 작성자가 현재 로그인 되어있는 사용자인지 판단하는 로직(소유자 확인용)
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
