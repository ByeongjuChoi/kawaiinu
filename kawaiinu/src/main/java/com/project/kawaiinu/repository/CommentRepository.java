package com.project.kawaiinu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.kawaiinu.entity.CommentsEntity;
import com.project.kawaiinu.entity.FeedEntity;
import com.project.kawaiinu.entity.UserEntity;

public interface CommentRepository extends JpaRepository<CommentsEntity, Long> {

	List<CommentsEntity> findByFeed(FeedEntity feed);
	List<CommentsEntity> findByFeedAndUser(FeedEntity feed, UserEntity user);
    void deleteByFeed(FeedEntity feed);
}
