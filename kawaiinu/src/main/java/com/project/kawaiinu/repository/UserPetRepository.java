package com.project.kawaiinu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.kawaiinu.entity.UserEntity;
import com.project.kawaiinu.entity.UserPetEntity;

public interface UserPetRepository extends JpaRepository<UserPetEntity, Long> {

	// でペットリスト情報を抽出して取得
    @Query("SELECT p FROM UserPetEntity p JOIN p.kawaiinuuserid u WHERE u.useremail = :useremail")
    List<UserPetEntity> userPetInfoSelect(@Param("useremail") String useremail);
}
