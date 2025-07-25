package egovframework.cms.board.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import egovframework.cms.board.service.BoardService;

@Component
public class ArchiveCleanupScheduler {

    @Autowired
    private BoardService boardService;

    // 매일 새벽 3시에 실행되는 스케줄 (cron 형식)
    @Scheduled(cron = "0 0 3 * * ?") // 매일 새벽 3시에 스케줄러 실행
    public void cleanupOldArchives() {
        boardService.deleteOldArchives();
        System.out.println("✔ [스케줄러] 90일 지난 아카이브 게시글 삭제 완료");
    }
}
