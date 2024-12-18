package com.project.kawaiinu.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDTO {
	private Long alarmid;				// alarmId
	private String userid;				// 유저 아이디
	private String usernickname;		// 유저 닉네임
	private String otherUserid;			// 댓글 or 좋아요 실행한 사용자
	private String otherusernickname;	// 댓글 or 좋아요 실행한 사용자의 닉네임
	private Timestamp createDate;	// 생성날짜
	private char likeOrComment;			// L: 좋아요, C: 댓글
	private String feedid;				// 피드 아이디
	private char readYN;				// Y: 읽음, N: 읽지않음
}
