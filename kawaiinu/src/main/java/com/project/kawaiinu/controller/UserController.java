package com.project.kawaiinu.controller;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.kawaiinu.dto.IntroduceRequestDTO;
import com.project.kawaiinu.dto.UserDTO;
import com.project.kawaiinu.dto.UserPetDTO;
import com.project.kawaiinu.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	// 会員登録
	// 회원가입
	@PostMapping(value = "/userentry"
				, consumes = "application/json"
				)
	public ResponseEntity saveUser(@Valid @RequestBody UserDTO userDTO, Errors error, Model model) throws ParseException {
		if (error.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getAllErrors());
		}
		
		// ユーザー名重複検査
		// 닉네임 중복검사 (true 중복일시)　
		if(userService.nickDupCheck(userDTO.getUsernickname()) == true) {
			String str = "{\"defaultMessage\":\"닉네임이 중복되었습니다.\"}";
			JSONParser JsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) JsonParser.parse(str);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject);
		}
		
		if(userService.isEmailDuplicate(userDTO.getUseremail()) == true) {
			String str = "{\"defaultMessage\":\"이메일이 중복되었습니다.\"}";
			JSONParser JsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) JsonParser.parse(str);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject);
		}
		
		userService.saveUser(userDTO);
	    return ResponseEntity.ok(userDTO);
	}
	
	// pet登録
	// 펫 등록
	@PostMapping(value = "/petentry"
			, consumes = "application/json"
			)
	public ResponseEntity saveUserPet(@Valid @RequestBody UserPetDTO userPetDTO, Errors error, Model model) throws ParseException {
		if (error.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getAllErrors());
		}
		userService.saveUserPet(userPetDTO);
	    return ResponseEntity.ok(userPetDTO);
	}
	
	// ユーザー情報照会	
	// 유저 정보 조회
	@PostMapping("/userInfoSelect")
	public List<UserDTO> userInfoSelect(@RequestBody Map<String, String> useremail) {
		String userEmail = useremail.get("useremail");
		return userService.userInfoSelect(userEmail);
	}
	
	// 펫 정보 조회
	@PostMapping("/userPetInfoSelect")
	public List<UserPetDTO> userPetInfoSelect(@RequestBody Map<String, String> useremail) {
		String userEmail = useremail.get("useremail");
		return userService.userPetInfoSelect(userEmail);
	}
	
	// 닉네임 변경
    @PutMapping("/nicknameChange")
    public ResponseEntity<String> changeNickname(@RequestBody Map<String, String> userInfo) {
    	String userId = userInfo.get("userid");
    	String newNickname = userInfo.get("newNickname");

        // 서비스 호출하여 닉네임 변경
        String responseMessage = userService.changeUserNickname(userId, newNickname);

        // 결과에 따른 응답 반환
        if (responseMessage.contains("성공")) {
            return ResponseEntity.ok(responseMessage);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
        }
    }
    
    @PatchMapping("/introduce")
    public ResponseEntity<Void> updateIntroduce(@RequestBody IntroduceRequestDTO introduceRequestDTO) {
        userService.updateUserIntroduce(introduceRequestDTO.getUserid(), introduceRequestDTO.getUserintroduce());
        return ResponseEntity.ok().build(); // 업데이트 성공
    }
}
