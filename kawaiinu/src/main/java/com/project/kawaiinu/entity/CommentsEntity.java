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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "kawaiinu_feed_comments")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class CommentsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentsid", nullable = false)
	private Long commentsid;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid") 	
    private UserEntity user;  		
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedid") 
    private FeedEntity feed;
    
	@Column(name = "comments")
	private String comments;	
	
	@Column(name = "commentscreatedate")
	private LocalDateTime commentscreatedate;
	
	public void setCommentscreatedate(LocalDateTime commentscreatedate) {
        this.commentscreatedate = commentscreatedate;
    }
}
