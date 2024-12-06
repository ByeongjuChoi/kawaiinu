package com.project.kawaiinu.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "feed_like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedLikeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long likeid;  // 좋아요 ID
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid")
	private UserEntity user;  // 좋아요를 누른 사용자
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feedid")
	private FeedEntity feed;  // 좋아요가 눌린 피드
	
	@Column(name = "likedate")
	private LocalDateTime likedate;  // 좋아요 눌린 날짜
}
