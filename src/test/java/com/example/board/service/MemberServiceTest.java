package com.example.board.service;

import com.example.board.dto.MemberDTO;
import com.example.board.entity.MemberEntity;
import com.example.board.member.MemberGrade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    MemberEntity memberEntity = null;

    @BeforeEach
    void beforeRegister() {
        memberEntity = new MemberEntity();

        memberEntity.setId(1L);
        memberEntity.setMemberId("idid");
        memberEntity.setMemberPw("1234");
        memberEntity.setMemberNic("홍길동");
        memberEntity.setMemberGd(MemberGrade.Bronze);
    }

    @Test
    @DisplayName("1. 회원가입")
    void register() {
        memberService.register(MemberDTO.toMemberDTO(memberEntity));
        MemberDTO memberDTO = memberService.findMemberId(memberEntity.getMemberId());
        assertEquals(memberDTO.getMemberId(), "idid");
    }

    // 테스트가 끝나면 테스트에 사용한 데이터 전체삭제
    @AfterEach
    void afterRegister() {
        memberService.deleteAllEntity();
    }

}