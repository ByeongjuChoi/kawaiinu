package com.project.kawaiinu.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedDTO {
	
	private String feedid;	// ポストid
	private String userid;	// ユーザーid
	
	private String picture;	// 写真
	
	private int feedlike;	// ポストのいいねの数
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // 日日の形式を指定
	private LocalDateTime feedcreatedate;
	
	private int feedstatus;		// 0活性化、1非活性化
	
	// 생성자 추가生成者
    public FeedDTO(String feedid, LocalDateTime feedcreatedate, Integer feedlike, int feedstatus, String picture, String userid) {
        this.feedid = feedid;
        this.feedcreatedate = feedcreatedate;
        this.feedlike = feedlike;
        this.feedstatus = feedstatus;
        this.picture = picture;
        this.userid = userid;
    }
}
