package com.project.kawaiinu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPetDTO {
	
	private Long   petid;
	
    private String petname;
    private String petbreed;
    private String petage;
    private String petgender;
    private String petsnack;
    private Double petweight;
    
    private String useremail; // 유저의 이메일을 통해 ID를 찾기 위해 사용
    private String kawaiinuuserid;  // 해당 펫의 주인 ID
    
}