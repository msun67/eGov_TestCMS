package egovframework.cms.member.web;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.cms.member.excel.MemberExcelUtil;
import egovframework.cms.member.service.impl.MemberServiceImpl;
import egovframework.cms.member.vo.MemberSearchVO;
import egovframework.cms.member.vo.SignupVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberServiceImpl memberService;
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping("/userList.do")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String userList(@ModelAttribute("searchVO") MemberSearchVO searchVO,
                           Model model) {

        if (searchVO.getPage() < 1) searchVO.setPage(1);

        int totalCnt   = memberService.getUserListCnt(searchVO);
        int pageSize   = searchVO.getSize();
        int totalPages = (int)Math.ceil((double)totalCnt / pageSize);
        if (searchVO.getPage() > totalPages && totalPages > 0) {
            searchVO.setPage(totalPages);
        }

        List<SignupVO> list = memberService.getUserList(searchVO);

        model.addAttribute("memberList", list);
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("page", searchVO.getPage());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", totalPages);

        return "member/userlist";
    }
	
	@GetMapping("/userListExcel.do")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<byte[]> downloadXlsx(@ModelAttribute MemberSearchVO searchVO) throws Exception{
		List<SignupVO> list = memberService.getUserListExcel(searchVO);
	    byte[] bytes = MemberExcelUtil.buildXlsxBytes(list);

	    String filename = java.net.URLEncoder.encode("회원목록.xlsx", java.nio.charset.StandardCharsets.UTF_8).replace("+", "%20");

	    return ResponseEntity.ok()
	        .header("Content-Disposition", "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + filename)
	        .header("Pragma", "no-cache")
	        .header("Expires", "0")
	        .contentType(org.springframework.http.MediaType.parseMediaType(
	            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
	        .body(bytes);
	}
	
	@GetMapping("/userListExcelXls.do")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<byte[]> downloadXls(@ModelAttribute MemberSearchVO searchVO) throws Exception {
		List<SignupVO> list = memberService.getUserListExcel(searchVO);
	    byte[] bytes = MemberExcelUtil.buildXlsBytes(list);

	    String filename = java.net.URLEncoder.encode("회원목록.xls", java.nio.charset.StandardCharsets.UTF_8).replace("+", "%20");

	    return ResponseEntity.ok()
	        .header("Content-Disposition", "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + filename)
	        .header("Pragma", "no-cache")
	        .header("Expires", "0")
	        .contentType(org.springframework.http.MediaType.parseMediaType("application/vnd.ms-excel"))
	        .body(bytes);
	}
	
	// 상세 조회 (사용자정보 수정 패널 로딩용)
    @GetMapping(value="/userItem.do", produces="application/json; charset=UTF-8")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public SignupVO userItem(@RequestParam String userUuid) throws Exception {
        return memberService.getUserByUuid(userUuid);
    }

    // 관리자 수정 (권한/비번/휴대폰)
    @PostMapping(value="/userUpdate.do", produces="application/json; charset=UTF-8")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Map<String,Object> userUpdate(
            @RequestParam String userUuid,
            @RequestParam(required=false) Integer userType,
            @RequestParam(required=false) String newPassword,
            @RequestParam(required=false) String mobile
    ) throws Exception {

        if (userType != null && (userType < 0 || userType > 2)) {
            return Map.of("ok", false, "msg", "권한 값 오류");
        }

        String encodedPw = null;
        if (newPassword != null && !newPassword.isBlank()) {
            if (newPassword.length() < 8) return Map.of("ok", false, "msg", "비밀번호는 8자 이상");
            encodedPw = passwordEncoder.encode(newPassword);
        }

        memberService.adminUpdateUser(userUuid, userType, encodedPw, mobile);
        return Map.of("ok", true);
    }

}
