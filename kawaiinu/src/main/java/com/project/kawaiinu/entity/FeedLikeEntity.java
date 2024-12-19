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
	private Long likeid;  // いいねID
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid")
	private UserEntity user;  // いいねボタンを押下したユーザー
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feedid")
	private FeedEntity feed;  // いいねボタンが押下されたポスト
	
	@Column(name = "likedate")
	private LocalDateTime likedate;  // いいねボタンが押下された日日
}
