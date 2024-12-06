package com.project.kawaiinu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.kawaiinu.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>{
	Optional<UserEntity> findByUseremail(String useremail); // 이메일로 유저 찾기
	boolean existsByUsernickname(String usernickname); // 닉네임 중복 검사
	
	@Query("SELECT u FROM UserEntity u WHERE u.useremail = :useremail")
	List<UserEntity> userInfoSelect(@Param("useremail") String useremail);
	
	@Modifying
    @Query("UPDATE UserEntity u SET u.userintroduce = :userintroduce WHERE u.userid = :userid")
    void updateUserIntroduce(@Param("userid") String userid, @Param("userintroduce") String userintroduce);

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u WHERE u.useremail = :useremail")
    boolean existsByUseremail(@Param("useremail") String useremail);
}

