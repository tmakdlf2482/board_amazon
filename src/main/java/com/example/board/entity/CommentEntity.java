package com.example.board.entity;

import com.example.board.dto.CommentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "comment_table")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "commentMemberWriter", referencedColumnName = "memberNic")
    private MemberEntity memberWriter; // MemberEntity 클래스에서 Long id; 임

    @Column
    private String commentContent;

    @Column
    @CreationTimestamp // 현재 시간을 찍어줌
    private Date commentDate;

    @ManyToOne
    @JoinColumn(name = "commentBoardNum") // 게시글 번호
    private BoardEntity boardNumber;

    public static CommentEntity toCommentEntity(CommentDTO commentDTO) {
        CommentEntity commentEntity = new CommentEntity();

        commentEntity.setCommentId(commentDTO.getCommentId()); // 댓글 인덱스
        commentEntity.setCommentContent(commentDTO.getCommentContent());
        commentEntity.setCommentDate(commentDTO.getCommentDate());
        commentEntity.setBoardNumber(commentDTO.getBoardNumber()); // 게시글 번호 (BoardEntity 객체)
        commentEntity.setMemberWriter(commentDTO.getMemberWriter()); // 댓글 쓴사람의 닉네임 (MemberEntity 객체)

        return commentEntity;
    }

}