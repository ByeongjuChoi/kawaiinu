package com.project.kawaiinu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntroduceRequestDTO {
	private String userid;			// ユーザーID
    private String userintroduce;	// 自己紹介の内容
}
