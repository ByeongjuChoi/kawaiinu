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
	
	@NotBlank(message = "ニックネームを入力して下さい。")
	@Size(min=2, max=10, message = "닉네임은 2~10 글자여야 합니다.")
	private String usernickname;
	
	@NotBlank(message = "メールを入力して下さい。")
	private String useremail;
	
	@NotBlank(message = "年齢を入力して下さい。")
	private String userage;
	
	@NotBlank(message = "性別を入力して下さい。")
	private String usergender;
	
	@Size(min=0, max=30, message = "30文字まで入力可能です。")
	private String userintroduce;
	
	private List<UserPetDTO> userpets = new ArrayList<>(); // ユーザーのペット情報を含む
}
