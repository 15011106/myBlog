package com.springas.springas2.repository;

import com.springas.springas2.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByBoardIdxOrderByModifiedAtDesc(Long id);
}