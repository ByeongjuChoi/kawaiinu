package com.project.kawaiinu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.kawaiinu.entity.FeedEntity;
import com.project.kawaiinu.entity.FeedLikeEntity;
import com.project.kawaiinu.entity.UserEntity;

public interface FeedLikeRepository extends JpaRepository<FeedLikeEntity, Long> {
	boolean existsByUserAndFeed(UserEntity user, FeedEntity feed);  // 사용자가 해당 피드에 좋아요를 눌렀는지 확인
    long countByFeed(FeedEntity feed);  // 해당 피드에 눌린 총 좋아요 수 조회
    void deleteByUserAndFeed(UserEntity user, FeedEntity feed);  // 사용자가 해당 피드에 눌렀던 좋아요 삭제
    void deleteByFeed(FeedEntity feed); // FeedEntity에 관련된 좋아요 삭제
}
