package egovframework.cms.board.service.impl;
import java.util.List;

import egovframework.cms.board.service.BoardMasterVO;

public interface BoardMasterDAO {
	void insertBoard(BoardMasterVO boardMasterVO);
    List<BoardMasterVO> selectBoardList();
    BoardMasterVO selectBoardByCode(String boardCode);
    void updateBoard(BoardMasterVO boardMasterVO);
    void deleteBoard(String boardCode);
    List<BoardMasterVO> selectBoardMasterList();
}
