package com.springas.springas2.controller;

import com.springas.springas2.dto.CommentRequestDto;
import com.springas.springas2.model.Board;
import com.springas.springas2.model.Comment;
import com.springas.springas2.repository.BoardRepository;
import com.springas.springas2.repository.CommentRepository;
import com.springas.springas2.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
public class CommentController {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public CommentController(CommentRepository commentRepository, BoardRepository boardRepository){
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }


    @PostMapping("/{id}/comment")
    public String createComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @ModelAttribute CommentRequestDto requestDto){
        Comment comment = new Comment(requestDto);
        Board board = boardRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );
        comment.setUser(userDetails.getUser());
        comment.setBoard(board);
        commentRepository.save(comment);
        return "redirect:/board/{id}";
    }

    @PutMapping("/{id}/comment/{commentId}")
    public String editComment(@PathVariable Long commentId, @ModelAttribute CommentRequestDto requestDto){
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        comment.setText(requestDto.getText());
        commentRepository.save(comment);
        return "redirect:/board/{id}";
    }

    @DeleteMapping("/{id}/comment/{commentId}")
    public String deleteComment(@PathVariable Long commentId){
        commentRepository.deleteById(commentId);
        return "redirect:/board/{id}";
    }
}
