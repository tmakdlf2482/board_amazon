package com.example.board.dto;

import com.example.board.entity.MemberEntity;
import com.example.board.member.MemberGrade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO {

    private Long id;
    private String memberId;
    private String memberPw;
    private String memberNic;
    private MemberGrade memberGd;
    private int memberVisitCnt;

    public static MemberDTO toMemberDTO(MemberEntity memberEntity) { // memberEntity -> memberDTO
        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberId(memberEntity.getMemberId());
        memberDTO.setMemberPw(memberEntity.getMemberPw());
        memberDTO.setMemberNic(memberEntity.getMemberNic());
        memberDTO.setMemberGd(memberEntity.getMemberGd());
        memberDTO.setMemberVisitCnt(memberEntity.getMemberVisitCnt());

        return memberDTO;
    }

}