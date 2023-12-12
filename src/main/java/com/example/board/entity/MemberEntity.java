package com.example.board.entity;

import com.example.board.dto.MemberDTO;
import com.example.board.member.MemberGrade;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="member_table")
public class MemberEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String memberId;

    @Column
    private String memberPw;

    @Column(unique = true)
    private String memberNic;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberGrade memberGd;

    @Column(columnDefinition = "int default 0")
    private int memberVisitCnt;

    // mappedBy를 쓰면 양방향 참조가 됨
    @OneToMany(mappedBy = "memberWriter") // CommentEntity의 commentWriter가 주인, MemberEntity가 노예
    private List<CommentEntity> writers = new ArrayList<>();

    @OneToMany(mappedBy = "boardMemberWriter")
    private List<BoardEntity> boardWriters = new ArrayList<>();

    public static MemberEntity toMemberEntity(MemberDTO memberDTO) { // memberDTO -> memberEntity
        MemberEntity memberEntity = new MemberEntity();

        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberId(memberDTO.getMemberId());
        memberEntity.setMemberPw(memberDTO.getMemberPw());
        memberEntity.setMemberNic(memberDTO.getMemberNic());
        memberEntity.setMemberVisitCnt(memberDTO.getMemberVisitCnt());
        memberEntity.setMemberGd(MemberGrade.Bronze);

        return memberEntity;
    }

}