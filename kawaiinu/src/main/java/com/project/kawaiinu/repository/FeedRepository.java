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
	
	// 피드 전체 불러오기
	@Query(value = """
				SELECT
			        	u.userid AS userid,
			        	f.feedid AS feedid,
			        	f.feedcreatedate AS feedcreatedate,
			        	f.feedlike AS feedlike,
			        	f.feedstatus AS feedstatus,
			        	fl.userid AS likeuserid,
			        	fl.feedid AS likefeedid 
			      FROM kawaiinu_user u
			      LEFT OUTER JOIN 
					(
						SELECT feedid, feedcreatedate, feedlike, feedstatus, userid
						  FROM kawaiinu_user_feed
						 WHERE feedstatus = 1
			        ) f 
					on u.userid = f.userid
			    LEFT OUTER JOIN
			        feed_like fl   
			            ON u.userid = fl.userid 
	        """, nativeQuery = true)
    List<Object[]> findAllFeedsWithLikes();
}
