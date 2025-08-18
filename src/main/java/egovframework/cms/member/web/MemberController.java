package egovframework.cms.member.web;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.cms.member.service.impl.MemberServiceImpl;
import egovframework.cms.member.vo.MemberSearchVO;
import egovframework.cms.member.vo.SignupVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberServiceImpl memberService;
	
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

}
