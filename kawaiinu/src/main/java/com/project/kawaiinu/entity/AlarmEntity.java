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
    private char readYN; // アラーム確認状態(Y/N)

    @Column(name = "likeOrComment", nullable = false)
    private char likeOrComment; // いいね/リプライの区分(L/C)

    @Column(name = "otherUserid", nullable = false)
    private String otherUserId; // いいね/リプライをしたユーザー

    @CreationTimestamp
    @Column(name = "createDate", nullable = false)
    private LocalDateTime createDate; 
}
