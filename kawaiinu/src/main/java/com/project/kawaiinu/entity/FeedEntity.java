package com.project.kawaiinu.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "kawaiinu_user_feed")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class FeedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedid", nullable = false)
	private Long feedid;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid") // 외래 키를 가리키는 칼럼명 지정
    private UserEntity kawaiinuuserfeedid;
    
	@Column(name = "picture")
	private String picture;		// 사진 (타입 미정)
	
	@Column(name = "feedlike")
	private int feedlike;		// 좋아요 수
	
	@Column(name = "feedcreatedate")
	private LocalDateTime feedcreatedate;	// 피드 생성 날짜	타입 date로 바꿔야함
	
	@Column(name = "feedstatus")
	private int feedstatus;		// 게시글 상태		0 비공개, 1 공개
	
	@OneToMany(mappedBy = "feed", fetch = FetchType.EAGER)
	private List<CommentsEntity> comments = new ArrayList<>();		// 코멘트	
	
    @OneToMany(mappedBy = "feed", fetch = FetchType.EAGER)
    private List<StrollCountEntity> strollCounts = new ArrayList<>();

	public void setUser(final UserEntity user) {
		this.kawaiinuuserfeedid = user;
	}
	
	// 댓글을 추가하는 메서드 (편리하게 추가할 수 있음)
	public void addComment(CommentsEntity comment) {
		this.comments.add(comment);
	}
	
	// 피드 상태를 변경하는 메서드
    public void setFeedStatus(int feedstatus) {
        this.feedstatus = feedstatus;
    }
}
