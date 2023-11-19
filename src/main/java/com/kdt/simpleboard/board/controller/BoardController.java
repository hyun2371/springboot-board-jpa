package com.kdt.simpleboard.board.controller;

import com.kdt.simpleboard.board.service.BoardService;
import com.kdt.simpleboard.common.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kdt.simpleboard.board.dto.BoardRequest.CreateBoardRequest;
import static com.kdt.simpleboard.board.dto.BoardRequest.ModifyBoard;
import static com.kdt.simpleboard.board.dto.BoardResponse.CreateBoardRes;
import static com.kdt.simpleboard.board.dto.BoardResponse.FindBoardRes;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<CreateBoardRes> createBoard(@Valid @RequestBody CreateBoardRequest request){
        CreateBoardRes response = boardService.createBoard(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}")
    public ResponseEntity<FindBoardRes> updateBoard(@PathVariable("id") Long boardId, @Valid @RequestBody ModifyBoard request) {
        FindBoardRes response = boardService.updateBoard(boardId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindBoardRes> findBoard(@PathVariable("id") Long boardId){
        FindBoardRes response = boardService.findById(boardId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<FindBoardRes>> findAll(Pageable pageable){
        PageResponse<FindBoardRes> response = boardService.findAll(pageable);
        return ResponseEntity.ok(response);
    }
}