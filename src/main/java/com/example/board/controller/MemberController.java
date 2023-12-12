package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.MemberDTO;
import com.example.board.service.BoardService;
import com.example.board.service.CommentService;
import com.example.board.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService; // @RequiredArgsConstructor 필요
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/board/login")
    public String login() {
        return "login";
    }

    @GetMapping("/board/register")
    public String regForm() {
        return "register";
    }

    @PostMapping("/board/register")
    public String register(@ModelAttribute MemberDTO memberDTO) { // Model 객체 찾아보기
        memberService.register(memberDTO);

        return "login";
    }

//    @PostMapping("/board/login")
//    public String login(@ModelAttribute MemberDTO memberDTO, HttpServletRequest httpServletRequest, BindingResult bindingResult) {
//        MemberDTO loginResult = memberService.login(memberDTO);
//
//        if (loginResult != null) { // 로그인 성공하면 닉네임이랑, 등급을 찍기 위한 것
//            httpServletRequest.getSession().invalidate(); // .invalidate()은 기존의 세션 제거
//            HttpSession session = httpServletRequest.getSession(true);
//
//            session.setAttribute("loginNic", loginResult.getMemberNic());
//            session.setAttribute("loginGd", loginResult.getMemberGd());
//            session.setAttribute("loginId", loginResult.getMemberId());
//
//            memberService.updateVisitCnt(loginResult); // 로그인 할때 마다 방문횟수 1씩 증가
//
//            return "redirect:/"; // redirect는 새로고침
//        }
//        else {
//            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 틀렸습니다.");
//        }
//
//        if (bindingResult.hasErrors()) {
//            return "login";
//        }
//
//        return "login";
//    }

    @GetMapping("/board/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate(); // 세션 제거
            return "redirect:/";
        }

        return "redirect:/";
    }

    @GetMapping("/board/mypage")
    public String myPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            String memberId = (String) session.getAttribute("loginId");
            MemberDTO memberDTO = memberService.findMemberId(memberId);
            model.addAttribute("member", memberDTO);

            String memberNic = (String) session.getAttribute("loginNic");
            List<BoardDTO> boardDTOList = boardService.findByBoardWriter(memberNic); // 회원 닉네임(nic)에 해당하는 boardDTO List를 가져옴
            model.addAttribute("writeList", boardDTOList);

            int boardWriteCnt = boardService.findByBoardWriteCnt(memberNic);
            model.addAttribute("writeCnt", boardWriteCnt);

            int commentWriteCnt = commentService.findByCommentWriteCnt(memberNic);
            model.addAttribute("commentCnt", commentWriteCnt);

            int memberVisitCnt = memberService.findMemberVisitCnt(memberId); // 방문횟수의 값을 가져온다.
            model.addAttribute("visitCnt", memberVisitCnt);
        }

        return "mypage";
    }

}