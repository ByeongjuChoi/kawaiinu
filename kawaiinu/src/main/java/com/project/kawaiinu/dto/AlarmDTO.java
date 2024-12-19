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
	private Long alarmid;				// アラームid
	private String userid;				// ユーザーid
	private String usernickname;		// ユーザーニックネーム
	private String otherUserid;			// リプライやいいねをしたユーザーid
	private String otherusernickname;	// リプライといいねをしたユーザーのニックネーム
	private Timestamp createDate;		// 生成日
	private char likeOrComment;			// L:いいね、C:リプライ
	private String feedid;				// ポストのID
	private char readYN;				// Y:読んだ状態、N:読んでない状態
}
