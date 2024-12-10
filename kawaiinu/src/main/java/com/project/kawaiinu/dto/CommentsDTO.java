package com.project.kawaiinu.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDTO {

	private Long commentsid;  	// 댓글 ID
	private String userid;     	// 댓글 작성자 ID (UserEntity의 userid)
	private String feedid;     	// 댓글이 달린 게시물 ID (FeedEntity의 feedid)
	private String comments; 	// 댓글 내용
	private String usernickname; // 유저 닉네임
	private LocalDateTime commentsCreatedDate;  // 댓글 작성 시간
	
	/*
	// 생성자, getter, setter 등
    public CommentsDTO(Long commentsid, String feedid, String userid, String comments, LocalDateTime commentscreatedate) {
    	this.commentsid = commentsid;
        this.feedid = feedid;
        this.userid = userid;
        this.comments = comments;
        this.commentsCreatedDate = commentscreatedate;
    }
    */

}
