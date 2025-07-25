package egovframework.cms.board.service;

import java.util.List;


public interface BoardService {
	List<BoardVO> getBoardList(SearchVO searchVO);
	int getBoardListCnt(SearchVO searchVO);
	BoardVO getBoardDetail(int boardId);
	void updateViewCount(int boardId);
	void insertBoard(BoardVO boardVO);
	void updateBoard(BoardVO boardVO);
	void deleteBoard(BoardVO boardVO)throws Exception; // 게시글 삭제
    void deleteOldArchives();          // 90일 지난 아카이브 삭제
    BoardVO getPrevPost(String createdAt, String boardCode);
    BoardVO getNextPost(String createdAt, String boardCode);
    
}
