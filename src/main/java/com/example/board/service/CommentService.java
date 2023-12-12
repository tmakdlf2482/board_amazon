package com.example.board.service;

import com.example.board.dto.CommentDTO;
import com.example.board.entity.CommentEntity;
import com.example.board.entity.MemberEntity;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public List<CommentDTO> findByBoardId(Long boardId) {
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardNumber_BoardId(boardId);
        List<CommentDTO> commentDTOList = new ArrayList<>();

        for (CommentEntity commentEntity : commentEntityList) {
            commentDTOList.add(CommentDTO.toCommentDTO(commentEntity));
        }

        return commentDTOList;
    }

    public void insert(Long boardId, String sessionId, CommentDTO commentDTO) {
        MemberEntity memberEntity = memberRepository.findByMemberId(sessionId).orElse(null);

        commentDTO.setBoardNumber(boardRepository.findById(boardId).orElse(null));
        commentDTO.setMemberWriter(memberEntity);

        commentRepository.save(CommentEntity.toCommentEntity(commentDTO));
    }

    public int findByCommentWriteCnt(String memberNic) {
        List<CommentEntity> commentEntityList = commentRepository.findAllByMemberWriter_MemberNic(memberNic);

        return commentEntityList.size();
    }

}