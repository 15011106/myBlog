package com.springas.springas2.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Setter
@Getter
@ToString

public class SignupRequestDto {

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z]).{3,10}",
            message = "닉네임은 최소 3자 이상 10자 이하이며 알파벳 대소문자, 숫자가 포함되어야 합니다.")
    private String username;

    @NotBlank(message = "패스워드는 필수 입력 값입니다.")
    @Size(min = 3, max = 10)
    private String password;


    @NotBlank(message = "같은 패스워드를 입력해 주세요.")
    @Size(min = 3, max = 10)
    private String passwordCheck;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email
    private String email;

    private Long kakaoId;
}