package egovframework.cms.board.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.cms.board.service.BoardFileService;
import egovframework.cms.board.service.BoardFileVO;
import egovframework.cms.board.service.BoardService;
import egovframework.cms.board.service.BoardVO;
import egovframework.cms.board.service.SearchVO;

@Service
public class BoardServiceImpl implements BoardService{

	private egovframework.cms.board.service.impl.BoardDAO boardDAO;

	public BoardServiceImpl(BoardDAO boardDAO) {
        this.boardDAO = boardDAO;
    }
	@Override
	public List<BoardVO> getBoardList(SearchVO searchVO){
	    return boardDAO.getBoardList(searchVO);
	}	

	@Override
	public int getBoardListCnt(SearchVO searchVO) {
	    return boardDAO.getBoardListCnt(searchVO);
	}
	
	@Override
	public BoardVO getBoardDetail(int boardId) {
	    return boardDAO.getBoardDetail(boardId);
	}
	
	@Override
	public void updateViewCount(int boardId){
		boardDAO.updateViewCount(boardId);
	}
	
	@Override
	public void insertBoard(BoardVO boardVO) {
		boardDAO.insertBoard(boardVO);
	}
	
	@Override
	public void updateBoard(BoardVO boardVO) {
		boardDAO.updateBoard(boardVO);
	}
	
	@Resource(name = "boardFileService")
	private BoardFileService boardFileService;
	
	@Override
    public void deleteBoard(BoardVO boardVO) throws Exception {
		int boardId = boardVO.getBoardId();

	    // 1. 해당 게시글의 첨부파일 정보 가져오기
	    List<BoardFileVO> fileList = boardDAO.findFilesByPostId(boardId);

	    // 2. 파일 ID 리스트 추출
	    List<Integer> fileIds = fileList.stream()
	                                    .map(BoardFileVO::getFileId)
	                                    .collect(Collectors.toList());

	    // 3. 첨부파일 삭제 (물리적 + DB)
	    if (!fileIds.isEmpty()) {
	        boardFileService.deleteFilesByIds(fileIds);
	    }
	    
        boardDAO.deleteBoard(boardVO.getBoardId());       // soft delete
        boardDAO.archiveBoard(boardVO);                   // archive
    }

    @Override
    public void deleteOldArchives() {
        boardDAO.deleteOldArchivedBoards();               // batch cleanup
    }
    
    @Override
    public BoardVO getPrevPost(String createdAt, String boardCode) {
        return boardDAO.getPrevPost(createdAt, boardCode);
    }

    @Override
    public BoardVO getNextPost(String createdAt, String boardCode) {
        return boardDAO.getNextPost(createdAt, boardCode);
    }
    
    
    // 대시보드용
    @Override
	public int countAllPosts() {
	    return boardDAO.countAllPosts();
	}
    @Override
    public List<BoardVO> findRecentByBoardCode(String boardCode, int limit) {
        return boardDAO.selectRecentByBoardCode(boardCode, limit);
    }
    @Override
    public int countTodayPosts() {
        return boardDAO.countTodayPosts();
    }
    @Override
    public List<BoardVO> findRecentWithFileCount(int limit) {
        return boardDAO.selectRecentWithFileCount(limit);
    }
    
}
