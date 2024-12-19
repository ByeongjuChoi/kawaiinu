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

	private Long commentsid;  					// リプライID
	private String userid;     					// リプライ作成者(UserEntityのUserid)
	private String feedid;     					// リプライが作成されたぽすとのid(FeedEntityのfeedid)
	private String comments; 					// リプライの内容
	private String usernickname; 				// ユーザーのニックネーム
	private LocalDateTime commentsCreatedDate;  // リプライ作成時間

}
