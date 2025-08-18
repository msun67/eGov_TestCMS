package egovframework.cms.board.service.impl;
import java.util.List;
import egovframework.cms.board.service.BoardMasterVO;

public interface BoardMasterDAO {
	String getNextBoardCode();
	void insertBoard(BoardMasterVO boardMasterVO);
    List<BoardMasterVO> selectBoardList();
    BoardMasterVO selectBoardByCode(String boardCode);
    void updateBoard(BoardMasterVO boardMasterVO);
    
    void softDeletePostsByBoardCode(String boardCode) throws Exception;
    void archivePostsByBoardCode(String boardCode) throws Exception;
    void deletePostsByBoardCode(String boardCode) throws Exception;
    void deleteBoard(String boardCode) throws Exception;
    
    List<BoardMasterVO> selectBoardMasterList();
    int updateUseYn(String boardCode, int useyn);
    BoardMasterVO getBoardInfo(String boardCode);
}
