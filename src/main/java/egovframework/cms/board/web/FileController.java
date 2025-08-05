package egovframework.cms.board.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.cms.board.service.BoardFileService;
import egovframework.cms.board.service.BoardFileVO;

@Controller
@RequestMapping("/file")
public class FileController {
	@Autowired
    private BoardFileService boardFileService;

    @GetMapping("/download.do")
    public void downloadFile(@RequestParam("fileId") int fileId, HttpServletResponse response) throws Exception {
        BoardFileVO file = boardFileService.getFileById(fileId);

        if (file == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File downloadFile = new File(file.getFilePath(), file.getSaveName());
        if (!downloadFile.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition",
            "attachment; filename=\"" + URLEncoder.encode(file.getOriginalName(), "UTF-8") + "\";");
        response.setContentLength((int) downloadFile.length());

        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(downloadFile));
             BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {

            byte[] buffer = new byte[8192];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }

}
