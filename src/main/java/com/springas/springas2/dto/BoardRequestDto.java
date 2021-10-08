package com.springas.springas2.dto;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class BoardRequestDto {
    private String username;
    private String title;
    private String content;

}
