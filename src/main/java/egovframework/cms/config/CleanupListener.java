package egovframework.cms.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

@WebListener
public class CleanupListener implements ServletContextListener {
	
	  @Override public void contextDestroyed(ServletContextEvent sce) {
	  AbandonedConnectionCleanupThread.checkedShutdown();
	  System.out.println("✔ MySQL AbandonedConnectionCleanupThread 정상 종료 완료"); }
	  
	  @Override public void contextInitialized(ServletContextEvent sce) { // 초기화 로직없음
	   }
	 
}
