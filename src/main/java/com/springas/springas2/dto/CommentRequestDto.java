package com.springas.springas2.dto;
import com.springas.springas2.model.Board;
import com.springas.springas2.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDto {
    private String text;
    private User user;
    private Board board;
}

