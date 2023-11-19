package com.kdt.simpleboard.board.service;

import com.kdt.simpleboard.board.domain.Board;
import com.kdt.simpleboard.board.dto.BoardMapper;
import com.kdt.simpleboard.board.dto.BoardRequest;
import com.kdt.simpleboard.board.repository.BoardRepository;
import com.kdt.simpleboard.common.dto.PageResponse;
import com.kdt.simpleboard.common.exception.CustomException;
import com.kdt.simpleboard.user.domain.User;
import com.kdt.simpleboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kdt.simpleboard.board.dto.BoardMapper.*;
import static com.kdt.simpleboard.board.dto.BoardRequest.ModifyBoard;
import static com.kdt.simpleboard.board.dto.BoardResponse.CreateBoardRes;
import static com.kdt.simpleboard.board.dto.BoardResponse.FindBoardRes;
import static com.kdt.simpleboard.common.exception.ErrorCode.Not_EXIST_BOARD;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserService userService;

    @Transactional
    public CreateBoardRes createBoard(BoardRequest.CreateBoardRequest request){
        User user = userService.getUserEntity(request.memberId());
        Board board = toBoardEntity(request, user);
        Board savedBoard = boardRepository.save(board);
        return toCreateBoardRes(savedBoard);
    }

    @Transactional
    public FindBoardRes updateBoard(Long boardId, ModifyBoard request){
        Board board = getBoardEntity(boardId);
        Board updatedBoard = board.updateBoardInfo(request.title(), request.content());
        return toFindBoardRes(updatedBoard);
    }


    public FindBoardRes findById(Long boardId) {
        Board board = getBoardEntity(boardId);
        return toFindBoardRes(board);
    }

    public PageResponse<FindBoardRes> findAll(Pageable pageable){
        Page<Board> pagedBoards = boardRepository.findAll(pageable);
        Page<FindBoardRes> pagedFindBoardRes = pagedBoards.map(BoardMapper::toFindBoardRes);
        return PageResponse.fromPage(pagedFindBoardRes);
    }

    public Board getBoardEntity(Long boardId){
        return boardRepository.findById(boardId).orElseThrow(() -> new CustomException(Not_EXIST_BOARD));
    }


}