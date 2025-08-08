package egovframework.cms.board.service;

import java.util.List;

public interface BoardMasterService {
	String getNextBoardCode();
	void createBoard(BoardMasterVO boardMasterVO);
    List<BoardMasterVO> getBoardList();
    BoardMasterVO getBoardByCode(String boardCode);
    void updateBoard(BoardMasterVO boardMasterVO);
    void removeBoardWithPosts(String boardCode) throws Exception;
    //void deleteBoard(String boardCode)throws Exception;
    List<BoardMasterVO> getBoardMasterList();
    void updateUseYn(String boardCode, int useyn);
    BoardMasterVO getBoardInfo(String boardCode);
}
