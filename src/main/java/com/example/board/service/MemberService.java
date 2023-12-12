package com.example.board.service;

import com.example.board.dto.MemberDTO;
import com.example.board.entity.MemberEntity;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void register(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public MemberDTO login(MemberDTO memberDTO) {
        // Optional을 써서 memberDTO memberID 값을 받아오고 만약에 id값이 있으면 비밀번호도 가져오는데, DTO(사용자가 입력한 값)이랑
        // Entity(실제 DB에 저장된 값)와 같은지 비교해, 같으면 DTO값을 반환
        // 만약에 id가 없거나 id는 있는데 비밀번호가 다르면 null 값을 반환
        Optional<MemberEntity> byMemberId = memberRepository.findByMemberId(memberDTO.getMemberId());

        if (byMemberId.isPresent()) { // id가 존재하면
            MemberEntity memberEntity = byMemberId.get();

            if (memberEntity.getMemberPw().equals(memberDTO.getMemberPw())) {
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);

                return dto;
            }
            else { // id가 존재하는데 비밀번호는 없을 경우
                return null;
            }
        }
        else { // id가 존재하지 않으면
            return null;
        }
    }

    public MemberDTO findMemberId(String memberId) {
        Optional<MemberEntity> optionalMemberEntity =  memberRepository.findByMemberId(memberId);

        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();

            return MemberDTO.toMemberDTO(memberEntity);
        }

        return null;
    }


    public int findMemberVisitCnt(String memberId) {
        // 1. 세션에서 받아온 memberId값으로 해당되는 사용자 정보(MemberEntity)를 가져옴
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberId(memberId); // repository에서 반환되는 값의 타입은 optional

        if (optionalMemberEntity.isPresent()) {
            // 2. Optional -> MemberEntity 타입으로 변환
            MemberEntity memberEntity = optionalMemberEntity.get();
            MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);

            return memberDTO.getMemberVisitCnt();
        }
        else
            return 0;
    }

    public void updateVisitCnt(MemberDTO loginResult) {
        // DTO에 있는 visitCount 1 증가
        // 증가시킨 DTO값을 Entity로
        // Repository에서 memberEntity 값 save
        loginResult.setMemberVisitCnt(loginResult.getMemberVisitCnt() + 1);
        MemberEntity memberEntity = MemberEntity.toMemberEntity(loginResult);
        memberRepository.save(memberEntity);
    }

    public void deleteAllEntity() {
        memberRepository.deleteAll();
    }

}