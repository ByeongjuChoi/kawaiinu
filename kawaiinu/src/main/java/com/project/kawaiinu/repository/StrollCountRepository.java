package com.project.kawaiinu.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.kawaiinu.entity.StrollCountEntity;
import com.project.kawaiinu.entity.UserEntity;

public interface StrollCountRepository extends JpaRepository<StrollCountEntity, Long>{

	 // 사용자와 날짜 범위에 해당하는 산책 여부가 존재하는지 확인
    boolean existsByUserAndStrollDateBetween(UserEntity user, LocalDateTime start, LocalDateTime end);
}
