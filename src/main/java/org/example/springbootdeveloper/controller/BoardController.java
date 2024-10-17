package org.example.springbootdeveloper.controller;

import org.example.springbootdeveloper.dto.BoardDto;
import org.example.springbootdeveloper.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }


    // 전체 게시물 조회
    @GetMapping
    public List<BoardDto> getAllBoards() {
        return boardService.getAllBoards();
    }

    // 특정 게시물 조회
    @GetMapping("/{id}")
    public BoardDto getBoardById(@PathVariable Long id) {
        return boardService.getBoardById(id);
    }

    // 게시물 등록
    @PostMapping
    public BoardDto createBoard(@RequestBody BoardDto boardDto) {
        return boardService.createBoard(boardDto);
    }

    // 게시물 수정
    @PutMapping("/{id}")
    public BoardDto updateBoard(@PathVariable Long id, @RequestBody BoardDto boardDto) {
        return boardService.updateBoard(id, boardDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }


}
