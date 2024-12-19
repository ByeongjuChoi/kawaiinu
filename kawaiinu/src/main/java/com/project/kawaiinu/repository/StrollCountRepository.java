package com.project.kawaiinu.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.kawaiinu.entity.StrollCountEntity;
import com.project.kawaiinu.entity.UserEntity;

public interface StrollCountRepository extends JpaRepository<StrollCountEntity, Long>{

	 // 該当する範囲の散歩の存否を確認
    boolean existsByUserAndStrollDateBetween(UserEntity user, LocalDateTime start, LocalDateTime end);
    List<StrollCountEntity> findByUser_Userid(String userid);
}
