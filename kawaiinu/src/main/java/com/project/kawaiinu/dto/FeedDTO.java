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
	
	private String feedid;	// 피드 아이디
	private String userid;	// 유저 아이디
	
	@NotBlank(message = "이미지를 넣어주세요.")
	private String picture;
	
	private int feedlike;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // 날짜 형식 지정
	private LocalDateTime feedcreatedate;
	
	private int feedstatus;		// 0활성화, 1비활성화
	
	// 생성자 추가
    public FeedDTO(String feedid, LocalDateTime feedcreatedate, Integer feedlike, int feedstatus, String picture, String userid) {
        this.feedid = feedid;
        this.feedcreatedate = feedcreatedate;
        this.feedlike = feedlike;
        this.feedstatus = feedstatus;
        this.picture = picture;
        this.userid = userid;
    }
}
