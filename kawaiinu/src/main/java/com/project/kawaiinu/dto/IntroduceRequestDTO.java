package com.project.kawaiinu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntroduceRequestDTO {
	private String userid;           // 사용자 ID
    private String userintroduce;  // 자기소개글
}
