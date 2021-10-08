package com.springas.springas2.service;

import com.springas.springas2.dto.BoardRequestDto;
import com.springas.springas2.model.Board;
import com.springas.springas2.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BoardService{

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }
    public void BoardSave(BoardRequestDto boardRequestDto){
        Board board = new Board(boardRequestDto);
        boardRepository.save(board);
    }

    public void BoardUpdate(long idx, BoardRequestDto boardRequestDto){
        Board updateBoard = boardRepository.getById(idx);
        updateBoard.setTitle(boardRequestDto.getTitle());
        updateBoard.setContent(boardRequestDto.getContent());
        boardRepository.save(updateBoard);

    }

    public Page<Board> findBoardList(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize());
        return boardRepository.findAll(pageable);
    }

    public Board findBoardByIdx(Long idx) {

        return boardRepository.findById(idx).orElse(new Board());
    }

    }