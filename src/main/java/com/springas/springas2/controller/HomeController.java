package com.springas.springas2.controller;
import com.springas.springas2.security.UserDetailsImpl;
import com.springas.springas2.service.BoardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final BoardService boardService;

    public HomeController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String home(@PageableDefault Pageable pageable, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("boardList", boardService.findBoardList(pageable));

        }
        else{
            model.addAttribute("username", "null");
            model.addAttribute("boardList", boardService.findBoardList(pageable));

        }
        return "index";
    }
}