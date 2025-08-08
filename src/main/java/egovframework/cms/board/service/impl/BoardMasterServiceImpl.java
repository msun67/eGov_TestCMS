package egovframework.cms.board.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.cms.board.service.BoardMasterService;
import egovframework.cms.board.service.BoardMasterVO;

@Service
public class BoardMasterServiceImpl implements BoardMasterService {
	 private final BoardMasterDAO boardMasterDAO;

	    public BoardMasterServiceImpl(BoardMasterDAO boardMasterDAO) {
	        this.boardMasterDAO = boardMasterDAO;
	    }

	    @Override
	    public String getNextBoardCode() {
	        return boardMasterDAO.getNextBoardCode();
	    }
	    
	    @Override
	    public void createBoard(BoardMasterVO boardMasterVO) {
	    	// use_yn 기본값(1) 보정: int라면 0이 들어올 수 있으므로 필요 시 보정
	        if (boardMasterVO.getUseyn() != 0 && boardMasterVO.getUseyn() != 1) {
	        	boardMasterVO.setUseyn(1);
	        }
	        // 다음 코드 조회 → INSERT (중복 시 재시도)
	        int maxRetry = 3;
	        for (int i = 0; i < maxRetry; i++) {
	            String code = boardMasterDAO.getNextBoardCode();
	            boardMasterVO.setBoardCode(code);
	            try {
	                boardMasterDAO.insertBoard(boardMasterVO);
	                return; // 성공
	            } catch (org.springframework.dao.DuplicateKeyException e) {
	                // 누가 먼저 선점한 경우 → 재시도
	                if (i == maxRetry - 1) throw e;
	            }
	        }
	    }

	    @Override
	    public List<BoardMasterVO> getBoardList() {
	        return boardMasterDAO.selectBoardList();
	    }

	    @Override
	    public BoardMasterVO getBoardByCode(String boardCode) {
	        return boardMasterDAO.selectBoardByCode(boardCode);
	    }
	    
	    @Override
	    public void updateBoard(BoardMasterVO boardMasterVO) {
	        boardMasterDAO.updateBoard(boardMasterVO);
	    }	    
	    
	    @Override
	    public List<BoardMasterVO> getBoardMasterList() {
	        return boardMasterDAO.selectBoardMasterList();
	    }
	    
	    @Override
	    public void updateUseYn(String boardCode, int useyn) {
	        // 0/1 정규화 (그 외 값 방지)
	        int normalized = (useyn == 1) ? 1 : 0;

	        int updated = boardMasterDAO.updateUseYn(boardCode, normalized);
	        if (updated == 0) {
	            // 없는 코드에 대한 방어: 호출측(컨트롤러)에서 메시지 처리 가능
	            throw new IllegalArgumentException("존재하지 않는 boardCode: " + boardCode);
	        }
	    }
	    
	    @Override
	    public BoardMasterVO getBoardInfo(String boardCode) {
	    	if (boardCode == null || boardCode.isEmpty()) return null;
	        return boardMasterDAO.getBoardInfo(boardCode);
	    }
	    
	    @Override
	    @Transactional
	    public void removeBoardWithPosts(String boardCode) throws Exception {
	        // 1. 해당 게시판의 게시글 Soft Delete
	        boardMasterDAO.softDeletePostsByBoardCode(boardCode);
	        // 2. 아카이브 테이블에 백업
	        boardMasterDAO.archivePostsByBoardCode(boardCode);
	        // 3. 게시글 물리 삭제 (FK 방지용)
	        boardMasterDAO.deletePostsByBoardCode(boardCode);
	        // 4. 게시판 물리 삭제
	        boardMasterDAO.deleteBoard(boardCode);
	    }

		/*
		 * @Override public void deleteBoard(String boardCode) throws Exception{
		 * boardMasterDAO.deleteBoard(boardCode); }
		 */
}
