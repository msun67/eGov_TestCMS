package egovframework.cms.board.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import egovframework.cms.board.service.BoardFileVO;
import egovframework.cms.board.service.BoardVO;
import egovframework.cms.board.service.SearchVO;

@Repository
public class BoardDAOImpl implements BoardDAO {
	
	private final SqlSession sqlSession;	
	@Autowired
	public BoardDAOImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }	
	private final String namespace = "egovframework.cms.board.mapper.BoardMapper";

	// 게시글 리스트 조회
	@Override
	public List<BoardVO> getBoardList(SearchVO searchVO){
	    return sqlSession.selectList(namespace + ".getBoardList", searchVO);
	}
	
	@Override
	public int getBoardListCnt(SearchVO searchVO){
	    return sqlSession.selectOne(namespace + ".getBoardListCnt", searchVO);
	}
	
	// 게시글 상세 조회
	@Override
	public BoardVO getBoardDetail(int boardId) {
		//System.out.println("📌 DAO에서 조회하는 boardId: " + boardId);
		 BoardVO board = sqlSession.selectOne(namespace + ".getBoardDetail", boardId);
	   /* if (board == null) {
	        System.out.println("❌ DAO에서 게시글 찾을 수 없음!");
	    } else {
	        System.out.println("✅ DAO에서 가져온 게시글 제목: " + board.getBoardTitle());
	    }*/
		return board;
	}
	
	// 게시글 조회수 증가
	@Override
	public void updateViewCount(int boardId) {
		sqlSession.update(namespace + ".updateViewCount", boardId);		
	}
	
	// 게시글 등록
	@Override
	public void insertBoard(BoardVO boardVO) {
		sqlSession.insert(namespace + ".insertBoard", boardVO);
	}
	
	//게시글 수정
	@Override
	public void updateBoard(BoardVO boardVO) {
		sqlSession.update(namespace + ".updateBoard", boardVO);
	}
	
	//게시글 삭제시 deleted_at 시간 업데이트, is_deleted True 처리
	 @Override
	 public void deleteBoard(int boardId) {
		 sqlSession.update(namespace + ".deleteBoard", boardId);
	 }
	 
	 //삭제한 게시글 아카이브 테이블에 보관
	 @Override
	 public void archiveBoard(BoardVO boardVO) {
	 	sqlSession.insert(namespace + ".archiveBoard", boardVO);
	 }
	 
	 //아카이브 테이블에 90일 보관 후 삭제
	 @Override
	 public void deleteOldArchivedBoards() {
	 	sqlSession.delete(namespace + ".deleteOldArchivedBoards");
	 }
	 
	 //이전글, 다음글
	 @Override
	 public BoardVO getPrevPost(String createdAt, String boardCode) {
	     return sqlSession.selectOne(namespace + ".getPrevPost", Map.of("createdAt", createdAt, "boardCode", boardCode));
	 }

	 @Override
	 public BoardVO getNextPost(String createdAt, String boardCode) {
	     return sqlSession.selectOne(namespace + ".getNextPost", Map.of("createdAt", createdAt, "boardCode", boardCode));
	 }
	 
	 
	//첨부파일 등록
	@Override
	public void insertFile(BoardFileVO boardfile) {
	    sqlSession.insert(namespace + ".insertFile", boardfile);	    
	}
	// 첨부파일 삭제
	@Override
	public void deleteFilesByIds(List<Integer> fileIds) {
		sqlSession.delete(namespace + ".deleteFilesByIds", fileIds);
	}
	// 게시글 상세보기시 첨부파일 목록 조회
	@Override
	public List<BoardFileVO> findFilesByPostId(int boardId) {
	    return sqlSession.selectList(namespace + ".findFilesByPostId", boardId);
	}
	// 기존 첨부파일 교체시 삭제할 대상으로 인식
	@Override
	public List<BoardFileVO> findFilesByIds(List<Integer> fileIds) {
	    return sqlSession.selectList(namespace + ".findFilesByIds", fileIds);
	}
	// 첨부파일 다운로드
	@Override
	public BoardFileVO getFileById(int fileId) throws Exception {
	    return sqlSession.selectOne(namespace + ".getFileById", fileId);
	}


	
	// 대시보드용
	@Override
	public int countAllPosts() {
		return sqlSession.selectOne(namespace + ".countAllPosts");
	}
	@Override
    public List<BoardVO> selectRecentByBoardCode(String boardCode, int limit) {
        Map<String, Object> p = new HashMap<>();
        p.put("boardCode", boardCode);
        p.put("limit", limit);
        return sqlSession.selectList(namespace + ".selectRecentByBoardCode", p);
    }
	 @Override
	 public int countTodayPosts() {
        return sqlSession.selectOne(namespace + ".countTodayPosts");
    }
	@Override
    public List<BoardVO> selectRecentWithFileCount(int limit) {
        Map<String, Object> p = new HashMap<>();
        p.put("limit", limit);
        return sqlSession.selectList(namespace + ".selectRecentWithFileCount", p);
    }
}
