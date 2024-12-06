package com.project.kawaiinu.service;

import java.util.List;

import com.project.kawaiinu.dto.UserDTO;
import com.project.kawaiinu.dto.UserPetDTO;

public interface UserService {
	
//	public void userSave(UserDTO user);
	public void saveUser(UserDTO userDTO);
//	public List<UserDTO> myInfoSelectAll(String userid);
//	public boolean nickDupCheck(String usernick);
	public void saveUserPet(UserPetDTO userPetDTO);
	public boolean nickDupCheck(String nickname);
	public List<UserDTO> userInfoSelect(String useremail);
	public List<UserPetDTO> userPetInfoSelect(String useremail);
	public String changeUserNickname(String userId, String newNickname);
	public void updateUserIntroduce(String userid, String userintroduce);
	public boolean isEmailDuplicate(String email);
}
