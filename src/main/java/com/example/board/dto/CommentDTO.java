package com.example.board.dto;

import com.example.board.entity.BoardEntity;
import com.example.board.entity.CommentEntity;
import com.example.board.entity.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommentDTO {

    private Long commentId;
    private String commentContent;
    private MemberEntity memberWriter;
    private Date commentDate;
    private BoardEntity boardNumber;

    public static CommentDTO toCommentDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setMemberWriter(commentEntity.getMemberWriter());
        commentDTO.setCommentDate(commentEntity.getCommentDate());
        commentDTO.setCommentContent(commentEntity.getCommentContent());

        return commentDTO;
    }

}