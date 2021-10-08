package com.springas.springas2.controller;
import com.springas.springas2.dto.SignupRequestDto;
import com.springas.springas2.model.User;
import com.springas.springas2.repository.UserRepository;
import com.springas.springas2.security.UserDetailsImpl;
import com.springas.springas2.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // 회원 로그인 페이지
    @GetMapping("/user/login")
    public String login(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("user", userDetails.getUser().getUsername());
        } else {
            model.addAttribute("user", "null");
        }
        model.addAttribute("requestDto", new SignupRequestDto());
        return "login";
    }

    // 회원 가입 페이지
    @GetMapping("/user/signup")
    public String signup(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("user", userDetails.getUser().getUsername());
        } else {
            model.addAttribute("user", "null");
        }
        model.addAttribute("requestDto", new SignupRequestDto());
        return "signup";
    }

    // 회원 가입 요청 처리
    @PostMapping("/user/signup")
    public String registerUser(@Valid @ModelAttribute("requestDto") SignupRequestDto requestDto, BindingResult bindingResult, Model model) {

        Optional<User> foundId = userRepository.findByUsername(requestDto.getUsername());
        Optional<User> foundEmail = userRepository.findByEmail(requestDto.getEmail());

        // ID 중복 확인
        if (foundId.isPresent()) {
            bindingResult.addError(new FieldError("requestDto", "username", "이미 존재하는 ID 입니다."));
        }

        // Password ID 포함 확인
        if (requestDto.getPassword().contains(requestDto.getUsername())) {
            bindingResult.addError(new FieldError("requestDto", "password", "ID는 패스워드에 사용할 수 없습니다."));
        }
        // Password 일치 확인
        if (!requestDto.getPassword().equals(requestDto.getPasswordCheck())) {
            bindingResult.addError(new FieldError("requestDto", "PasswordCheck", "패스워드가 일치하지 않습니다."));
        }

        // Email 중복 확인
        if (foundEmail.isPresent()) {
            bindingResult.addError(new FieldError("requestDto", "email", "이미 존재하는 Email 입니다."));
        }

        if (bindingResult.hasErrors()
        ) {
            model.addAttribute("user", "null");
            return "signup";
        }
        userService.registerUser(requestDto);
        return "redirect:/user/login";
    }

    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(String code) {
        // authorizedCode: 카카오 서버로부터 받은 인가 코드
        userService.kakaoLogin(code);

        return "redirect:/";
    }
}
    // 로그인 체크
//    @GetMapping("/api/userCheck")
//    public String userCheck(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        String username = null;
//        if (userDetails != null) {
//            return userDetails.getUser().getUsername();
//        } else {
//            return username;
//        }
//    }