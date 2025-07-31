package egovframework.cms.board.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.cms.board.service.BoardFileVO;
import egovframework.cms.board.service.BoardVO;
import egovframework.cms.board.service.SearchVO;

public interface BoardDAO {
	List<BoardVO> getBoardList(SearchVO searchVO);
	
	int getBoardListCnt(SearchVO searchVO);
	
	BoardVO getBoardDetail(int boardId);
	
	void updateViewCount(int boardId);
	
	void insertBoard(BoardVO boardVO);
	
	void updateBoard(BoardVO boardVO);
	
	void deleteBoard(int boardId);
	
    void archiveBoard(BoardVO boardVO);
    
    void deleteOldArchivedBoards();
    
    //이전글, 다음글은 파라미터 2개
    BoardVO getPrevPost(@Param("createdAt") String createdAt, @Param("boardCode") String boardCode);
    BoardVO getNextPost(@Param("createdAt") String createdAt, @Param("boardCode") String boardCode);
    
    //첨부파일삽입
    void insertFile(BoardFileVO boardfile);
    
    //게시글 삭제시 모든 첨부파일 삭제할때사용
    List<BoardFileVO> getFileListByBoardId(int boardId);
    
    BoardFileVO getFileById(int fileId);//Mapper 구현안되어있음.
    
    void deleteFilesByIds(List<Integer> fileIds);
    
    //특정파일만삭제할때사용
    List<BoardFileVO>getFilesByIds(List<Integer> fileIds);
    
    List<Integer> getFileIdsByBoardId(int boardId);
    
    
    
    // 대시보드용 
	int countAllPosts();
	List<BoardVO> selectRecentByBoardCode(String boardCode, int limit);
	int countTodayPosts();
	List<BoardVO> selectRecentWithFileCount(int limit);
}
