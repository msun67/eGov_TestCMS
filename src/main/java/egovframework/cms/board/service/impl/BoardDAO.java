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
    
    
    // 첨부파일등록
    void insertFile(BoardFileVO boardfile);    
    // 기존 첨부파일 교체 시 삭제할 대상으로 입력
    void deleteFilesByIds(List<Integer> fileIds);
    // 게시글 상세보기시 첨부파일 목록 조회
    List<BoardFileVO> findFilesByPostId(int boardId);
    // 기존 첨부파일 교체시 삭제할 대상으로 인식
    List<BoardFileVO>findFilesByIds(List<Integer> fileIds);
    // 첨부파일다운로드
    BoardFileVO getFileById(int fileId) throws Exception;

    
    
    
    
    // 대시보드용 
	int countAllPosts();
	List<BoardVO> selectRecentByBoardCode(String boardCode, int limit);
	int countTodayPosts();
	List<BoardVO> selectRecentWithFileCount(int limit);
	// 내가쓴글
	int selectBoardListCntByAuthor(@Param("search") SearchVO searchVO,
            @Param("authorUuid") String authorUuid);
	List<BoardVO> selectBoardListByAuthor(@Param("search") SearchVO searchVO,
	                   @Param("authorUuid") String authorUuid);
}
