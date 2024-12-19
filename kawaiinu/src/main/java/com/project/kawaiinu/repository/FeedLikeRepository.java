package com.project.kawaiinu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.kawaiinu.entity.FeedEntity;
import com.project.kawaiinu.entity.FeedLikeEntity;
import com.project.kawaiinu.entity.UserEntity;

public interface FeedLikeRepository extends JpaRepository<FeedLikeEntity, Long> {
	boolean existsByUserAndFeed(UserEntity user, FeedEntity feed);  // 該当するポストにユーザーがいいねを押下したかを確認
    long countByFeed(FeedEntity feed);  // 該当するポストのいいねの総個数の情報を抽出して取得
    void deleteByUserAndFeed(UserEntity user, FeedEntity feed);  // ユーザーが該当するポストに押下したいいねを削除
    void deleteByFeed(FeedEntity feed); // FeedEntityと関われたいいねを削除
}
