package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.CommentDTO;
import com.example.board.service.BoardService;
import com.example.board.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;



import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/")
    public String home(Model model) {
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);

        return "index";
    }

    // DB에서 실제 값을 가져오려면 - 쿼리문 사용
    // 실제 쿼리문 쓸 수 있게 해주는 JPA의 메소드를 호출
    // Entity로 컬럼을 DB컬럼이랑 1:1로 매핑 시켜줘서 필요한 값을 가져오게 만듬
    // 가져오고 하는 중간과정에 DTO를 사용하고
    // 가져오는 실제 기능을 Service로 구현
    // Controller에서 Service를 호출해서 사이트가 접속되면 리스트를 바로 출력할 수 있게끔 만들어줌
    @GetMapping("/board/spr_board")
    public String sprBoardForm(Model model, @PageableDefault(page = 1) Pageable pageable) { // 1페이지부터 보여진다. 만약 @PageableDefault(page = 2)로 하면 2페이부터 보여진다.
//        List<BoardDTO> boardDTOList = boardService.findAll();
//        model.addAttribute("boardList", boardDTOList); // 뷰 페이지에 값을 띄워주기 위함 (thymeleaf에서 "boardList" 이름으로 써야함)

        Page<BoardDTO> boardDTOPage = boardService.paging(pageable);

        int startPage = Math.max(1, pageable.getPageNumber() - 2); // 매개변수 2개 중 큰 숫자를 고른다. getPageNumber()는 현재 보고 있는 페이지
        int endPage = Math.min(startPage + 4, boardDTOPage.getTotalPages()); // 매개변수 2개 중 작은 숫자를 고른다. getTotalPages는 전체 페이지 개수

        model.addAttribute("boardList", boardDTOPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "spr_board";
    }

    // 게시판 번호(boardId값)로 게시글 정보를 가져오기
    // Service.findByBoardId라는 메서드를 만들어서 Entity - Repository
    // select 쿼리문을 써서 게시글 번호에 해당하는 값을 받아오기 (Optional - null값 받아오기 위함)
    // Service안에서 Entity로부터 받아온 값이 있는지 체크해서 값이 존재하면 DTO로 변환시켜서 Controller에 돌려줌
    // model.addAttribute를 사용해서 view 페이지에서 thymeleaf로 전달
    // 게시판 뷰 페이지 이동
    @GetMapping("/board/spr_board/{boardId}")
    public String sprBoardView(@PathVariable Long boardId, Model model, HttpServletRequest request) {
        BoardDTO boardDTO = boardService.findByBoardId(boardId);
        List<CommentDTO> commentDTOList = commentService.findByBoardId(boardId);

        HttpSession session = request.getSession(false);
        boolean sessionCheck = (session != null);

        boardDTO.setBoardCommentCnt((long)commentDTOList.size());

        boardService.updateViewCnt(boardId);

        model.addAttribute("commentList", commentDTOList);
        model.addAttribute("board", boardDTO); // boardDTO를 model로 바꾼다.
        model.addAttribute("sessionCheck", sessionCheck);

        return "spr_board_view";
    }

    @GetMapping("/board/write")
    public String boardWrite(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if(session != null){
            return "write";
        }

        return "login";
    }

    @PostMapping("/board/write")
    public String boardWriteForm(@ModelAttribute BoardDTO boardDTO, HttpServletRequest request, MultipartFile file) throws Exception {
        HttpSession session = request.getSession(false);
        String mbId = (String)(session.getAttribute("loginId"));

        boardService.write(boardDTO, mbId, file);

        return "redirect:/board/spr_board";
    }

//    <게시글 삭제>
//
//    게시글 번호 받아와야함 - 어떤 글을 지울지
//    => view 페이지 타임리프 문법으로 게시글 번호(boardId)값을 넘겨줘
//
//    Controller에서 GetMapping으로 boardId값으로 접속된 페이지 연결
//    boardId값을 db에서 찾아와야함 (Service에서 찾는 기능 findbyBoardId();을 해야함)
//
//    Service는 entity, repository를 부르고 (select * from board_table where boardId = ?;)
//    DTO <- 이 부분은 지우려는 사람이 맞는지 체크할려고 함
//    게시글 번호 boardDTO.getBoardId(); 값을 가져옴, 가져온 번호에 대해서 삭제(delete);
    @GetMapping("/board/delete/{id}")
    public String boardDelete(@PathVariable Long id, HttpServletRequest request) {
        BoardDTO boardDTO = boardService.findByBoardId(id);
        HttpSession session = request.getSession(false);

        if (session != null) {
            if (session.getAttribute("loginNic").equals(boardDTO.getBoardMemberWriter().getMemberNic())) {
                boardService.deleteByBoardDTO(boardDTO);
                return "redirect:/board/spr_board"; // 자기 자신이 쓴 글을 자기 자신이 지우는 경우
            }
            else {
                return "redirect:/board/spr_board"; // 자기 자신이 쓸 글이 아닌 글을 지우려고 할 때
            }
        }
        else {
            return "redirect:/board/login"; // 아예 로그인 조차 하지 않았는데, 글을 삭제하려는 경우
        }
    }

    @GetMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable Long id, HttpServletRequest request, Model model) {
        BoardDTO boardDTO = boardService.findByBoardId(id);
        HttpSession session = request.getSession(false);

        if (session != null) {
            if (session.getAttribute("loginNic").equals(boardDTO.getBoardMemberWriter().getMemberNic())) {
                model.addAttribute("boardList", boardDTO);

                return "update";
            }
            else {
                return "redirect:/board/spr_board"; // 다른 사람이 글 쓴걸 수정하려고 할 때
            }
        }
        else {
            return "redirect:/board/login";
        }
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdateForm(@PathVariable Long id, BoardDTO boardDTO) {
        // 기존 게시글 내용을 가져온다. (id 값에 해당하는 게시글)
        BoardDTO exDTO = boardService.findByBoardId(id); // exDTO는 기존 게시글 내용, boardDTO는 새로 작성한 게시글 내용
        boardService.update(exDTO, boardDTO);

        return "redirect:/board/spr_board";
    }

    @GetMapping("/board/spr_board/{id}/like")
    public String boardLike(@PathVariable Long id, HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            BoardDTO boardDTO = boardService.findByBoardId(id);
            boardService.like(boardDTO);

            return "redirect:/board/spr_board/{id}";
        }
        else {
            return "redirect:/board/login";
        }
    }

    @GetMapping("/board/spr_board/{id}/dislike")
    public String boardDislike(@PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            BoardDTO boardDTO = boardService.findByBoardId(id);
            boardService.dislike(boardDTO);

            return "redirect:/board/spr_board/{id}";
        }
        else {
            return "redirect:/board/login";
        }
    }

}