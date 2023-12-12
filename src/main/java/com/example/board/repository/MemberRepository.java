package com.example.board.repository;

import com.example.board.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
     Optional<MemberEntity> findByMemberId(String memberId); // select memberId from member_table; 와 같음
}