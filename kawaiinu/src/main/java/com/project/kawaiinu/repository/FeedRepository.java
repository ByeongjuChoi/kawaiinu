package com.project.kawaiinu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.kawaiinu.entity.FeedEntity;

public interface FeedRepository extends JpaRepository<FeedEntity, String> {

	// 내 피드 정보 조회
	@Query(value = "SELECT a.feedid "
			+ "   ,a.feedcreatedate "
			+ "   ,a.feedlike "
			+ "   ,a.feedstatus "
			+ "   ,a.picture "
			+ "   ,a.userid "
	 		+ "FROM kawaiinu_user_feed a "
	 		+ "WHERE a.userid = :userid"
	 		, nativeQuery = true)
	List<Object[]> myFeedSelect(@Param("userid") String userid);
	
}
