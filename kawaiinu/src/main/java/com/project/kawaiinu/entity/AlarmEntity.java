package com.project.kawaiinu.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "kawaiinu_alarm")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class AlarmEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "alarmid", nullable = false)
	private Long alarmid;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid", nullable = false)	// fk
	private UserEntity user;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedid", nullable = false) // fk
    private FeedEntity feed;
	
	@Column(name = "readYN", nullable = false)
    private char readYN; // 읽기 여부 (Y/N)

    @Column(name = "likeOrComment", nullable = false)
    private char likeOrComment; // 좋아요/댓글 구분 (L/C)

    @Column(name = "otherUserid", nullable = false)
    private String otherUserId; // 좋아요/댓글을 수행한 사용자 ID

    @CreationTimestamp
    @Column(name = "createDate", nullable = false)
    private LocalDateTime createDate; // 알람 생성 날짜
}
