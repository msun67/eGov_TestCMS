package egovframework.cms.board.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import egovframework.cms.board.service.BoardMasterService;
import egovframework.cms.board.service.BoardMasterVO;

@Service
public class BoardMasterServiceImpl implements BoardMasterService {
	 private final BoardMasterDAO boardMasterDAO;

	    public BoardMasterServiceImpl(BoardMasterDAO boardMasterDAO) {
	        this.boardMasterDAO = boardMasterDAO;
	    }

	    @Override
	    public void createBoard(BoardMasterVO boardMasterVO) {
	        boardMasterDAO.insertBoard(boardMasterVO);
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
	    public void deleteBoard(String boardCode) {
	        boardMasterDAO.deleteBoard(boardCode);
	    }
	    
	    @Override
	    public List<BoardMasterVO> getBoardMasterList() {
	        return boardMasterDAO.selectBoardMasterList();
	    }
}
