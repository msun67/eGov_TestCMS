package egovframework.cms.board.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import egovframework.cms.board.service.BoardMasterVO;

@Repository
public class BoardMasterDAOImpl implements BoardMasterDAO{
	
	 private final SqlSession sqlSession;
	    private final String namespace = "egovframework.cms.board.mapper.BoardMasterMapper";

	    public BoardMasterDAOImpl(SqlSession sqlSession) {
	        this.sqlSession = sqlSession;
	    }
	    
	    @Override
	    public String getNextBoardCode() {
	        return sqlSession.selectOne(namespace + ".getNextBoardCode");
	    }

	    @Override
	    public void insertBoard(BoardMasterVO boardMasterVO) {
	        sqlSession.insert(namespace + ".insertBoard", boardMasterVO);
	    }

	    @Override
	    public List<BoardMasterVO> selectBoardList() {
	        return sqlSession.selectList(namespace + ".selectBoardList");
	    }

	    @Override
	    public BoardMasterVO selectBoardByCode(String boardCode) {
	        return sqlSession.selectOne(namespace + ".selectBoardByCode", boardCode);
	    }
	    
	    @Override
	    public void updateBoard(BoardMasterVO boardMasterVO) {
	        sqlSession.update(namespace + ".updateBoard", boardMasterVO);
	    }
	    
	    @Override
	    public void softDeletePostsByBoardCode(String boardCode) throws Exception {
	    	sqlSession.update(namespace + ".softDeletePostsByBoardCode", boardCode);
	    }

	    @Override
	    public void archivePostsByBoardCode(String boardCode) throws Exception {
	    	sqlSession.insert(namespace + ".archivePostsByBoardCode", boardCode);
	    }
	    
	    @Override
	    public void deletePostsByBoardCode(String boardCode) throws Exception {
	    	sqlSession.delete(namespace + ".deletePostsByBoardCode", boardCode);
	    }
	    
	    @Override
	    public void deleteBoard(String boardCode) throws Exception{
	        sqlSession.delete(namespace + ".deleteBoard", boardCode);
	    }
	    
	    @Override
	    public List<BoardMasterVO> selectBoardMasterList() {
	        return sqlSession.selectList(namespace + ".selectBoardMasterList");
	    }
	    
	    @Override
	    public int updateUseYn(String boardCode, int useyn) {
	        java.util.Map<String, Object> param = new java.util.HashMap<>();
	        param.put("boardCode", boardCode);
	        param.put("useyn", useyn);
	        return sqlSession.update(namespace + ".updateUseYn", param);
	    }
	    
	    @Override
	    public BoardMasterVO getBoardInfo(String boardCode) {
	        return sqlSession.selectOne(namespace + ".getBoardInfo", boardCode);
	    }
}
