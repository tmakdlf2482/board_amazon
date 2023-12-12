package com.example.board.repository;

import com.example.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // Optional<BoardEntity> findByBoardId(String boardId); // select boardId from board_table; 와 같음
    List<BoardEntity> findAllByBoardMemberWriter_MemberNic(String memberNic);
}