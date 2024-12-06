package com.project.kawaiinu.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private String userid;
	
	@NotBlank(message = "닉네임을 입력해주세요.")
	@Size(min=2, max=10, message = "닉네임은 2~10 글자여야 합니다.")
	private String usernickname;
	
	@NotBlank(message = "이메일을 입력해주세요.")
	private String useremail;
	
	@NotBlank(message = "나이를 입력해주세요.")
	private String userage;
	
	@NotBlank(message = "성별을 입력해주세요.")
	private String usergender;
	
	@Size(min=0, max=30, message = "30자까지 등록 가능합니다.")
	private String userintroduce;
	
	private List<UserPetDTO> userpets = new ArrayList<>(); // 유저의 펫 정보 포함
}
