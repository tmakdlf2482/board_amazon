package com.example.board.repository;

import com.example.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByBoardNumber_BoardId(Long boardId);

    List<CommentEntity> findAllByMemberWriter_MemberNic(String memberNic);
}