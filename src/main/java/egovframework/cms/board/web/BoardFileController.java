package egovframework.cms.board.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.cms.board.service.BoardFileService;
import egovframework.cms.board.service.BoardFileVO;

@Controller
@RequestMapping("/file")
public class BoardFileController {
	private final BoardFileService boardFileService;

    public BoardFileController(BoardFileService boardFileService) {
        this.boardFileService = boardFileService;
    }

    @GetMapping("/download.do")
    public void downloadFile(@RequestParam("fileId") int fileId, HttpServletResponse response) throws Exception {
    	System.out.println("🔥 download.do 요청 들어옴 fileId = " + fileId);
    	// 1. DB에서 파일 정보 조회
        BoardFileVO fileVO = boardFileService.getFileById(fileId);
        System.out.println("📂 조회된 fileVO = " + fileVO);

        if (fileVO == null || fileVO.isDeleted()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일을 찾을 수 없습니다.");
            return;
        }

        // 2. 실제 파일 경로
        File file = new File(fileVO.getFilePath(), fileVO.getSaveName());

        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일이 존재하지 않습니다.");
            return;
        }

        // 3. 응답 헤더 설정
        response.setContentType("application/octet-stream");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + URLEncoder.encode(fileVO.getOriginalName(), "UTF-8").replaceAll("\\+", "%20") + "\"");

        // 4. 파일 스트림 전송
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {

            byte[] buffer = new byte[4096];
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            out.flush();
        }
    }

}
