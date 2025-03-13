package com.mycompany.teamproject9.service;

import com.mycompany.teamproject9.dto.Board;
import com.mycompany.teamproject9.repository.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardMapper boardMapper;

    // 게시글 전체 조회
    public List<Board> getAllBoards() {
        return boardMapper.findAll();
    }

    // 게시글 작성
    public boolean createBoard(Board board) {
        return boardMapper.insert(board) > 0;
    }

    // 게시글 상세 조회
    public Board getBoardById(int boardId) {
        return boardMapper.findById(boardId);
    }

    // 게시글 삭제
    public boolean deleteBoard(int boardId) {
        return boardMapper.delete(boardId) > 0;
    }

    // 게시글 수정
    public boolean updateBoard(Board board) {
        return boardMapper.update(board) > 0;
    }
}