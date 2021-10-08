package com.springas.springas2.controller;

import com.springas.springas2.dto.BoardRequestDto;
import com.springas.springas2.model.Board;
import com.springas.springas2.model.Comment;
import com.springas.springas2.repository.BoardRepository;
import com.springas.springas2.repository.CommentRepository;
import com.springas.springas2.security.UserDetailsImpl;
import com.springas.springas2.service.BoardService;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
//@Restcontroller check this for
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public BoardController(BoardService boardService, BoardRepository boardRepository, CommentRepository commentRepository) {
        this.boardService = boardService;
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
    }

    // 게시판 조회
    @GetMapping("/")
    public String list(@PageableDefault Pageable pageable, Model model) {
        model.addAttribute("boardList", boardService.findBoardList(pageable));
        return "index";
    }

    // 게시글 작성페이지
    @GetMapping("/post")
    public String post(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {

        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
        } else {
            model.addAttribute("username", "null");
        }

        BoardRequestDto board = new BoardRequestDto();
        model.addAttribute("board", board);
        return "post";
    }


    //   게시글 작성 전송 이렇게 작성해도 가능한 (key, value를 받아올 때 form에서 urlencoded방식으로 던질때와 Json 두개 제공할때)
    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String write(@AuthenticationPrincipal UserDetailsImpl userDetails, BoardRequestDto boardRequestDto, Model model) {

        boardRequestDto.setUsername(userDetails.getUsername());
        boardService.BoardSave(boardRequestDto);
        return "redirect:/";
    }

//    // 게시글 작성 전송 (ModelAttribute로 객체받을때)
//    @PostMapping("/post")
//    public String write(@AuthenticationPrincipal UserDetailsImpl userDetails ,@ModelAttribute BoardRequestDto boardRequestDto) {
//
//        boardRequestDto.setUsername(userDetails.getUsername());
//        boardService.BoardSave(boardRequestDto);
//        return "redirect:/";
//    }


    //게시물 상세
    @GetMapping({"/{idx}"})
    public String boardDetail(@PathVariable Long idx, @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        List<Comment> comment = commentRepository.findByBoardIdxOrderByModifiedAtDesc(idx);
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUser().getUsername());
        } else {
            model.addAttribute("username", "null");
        }
        model.addAttribute("editcomment",new Comment());
        model.addAttribute("postcomment",new Comment());
        model.addAttribute("comment", comment);
        model.addAttribute("board", boardService.findBoardByIdx(idx));
        return "boardDetail";
    }

    //   // 게시물생성
//   @PostMapping("/post")
//   public ResponseEntity<?> postBoard(@RequestBody BoardRequestDto boardRequestDto) {
//       boardService.BoardSave(boardRequestDto);
//
//       return new ResponseEntity<>("{}", HttpStatus.CREATED);
//   }
    /*
     * 게시글 수정
     */
    @GetMapping("/{idx}/edit")
    public String getBoardEdit(@PathVariable Long idx, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Board board = boardService.findBoardByIdx(idx);
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUser().getUsername());
        } else {
            model.addAttribute("username", "null");
        }
        model.addAttribute("board", board);
        return "boardEdit";
    }

    @PutMapping("/{idx}/edit")
    public String boardUpdate(@PathVariable Long idx, @ModelAttribute BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        requestDto.setUsername(userDetails.getUsername());
        boardService.BoardUpdate(idx, requestDto);
        return "redirect:/";

    }

     // 게시글 삭제
    @DeleteMapping("/delete/{idx}")
    public String boardDelete(@PathVariable Long idx){
        boardRepository.deleteById(idx);
        return "redirect:/";
    }

}