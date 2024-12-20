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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.kawaiinu.dto.IntroduceRequestDTO;
import com.project.kawaiinu.dto.StrollDTO;
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
	@PostMapping(value = "/userentry", consumes = "application/json")
	public ResponseEntity saveUser(@Valid @RequestBody UserDTO userDTO, Errors error, Model model) throws ParseException {
		if (error.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getAllErrors());
		}
		
		// ユーザー名重複検査　
		if(userService.nickDupCheck(userDTO.getUsernickname()) == true) {
			String str = "{\"defaultMessage\":\"ニックネームが重複されました。\"}";
			JSONParser JsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) JsonParser.parse(str);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject);
		}
		
		if(userService.isEmailDuplicate(userDTO.getUseremail()) == true) {
			String str = "{\"defaultMessage\":\"メールアドレスが重複されました。\"}";
			JSONParser JsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) JsonParser.parse(str);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonObject);
		}
		
		userService.saveUser(userDTO);
	    return ResponseEntity.ok(userDTO);
	}
	
	// pet登録
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
	
	// ユーザー情報照会	ユーザー情報を抽出して取得
	@PostMapping("/userInfoSelect")
	public List<UserDTO> userInfoSelect(@RequestBody Map<String, String> useremail) {
		String userEmail = useremail.get("useremail");
		return userService.userInfoSelect(userEmail);
	}
	
	// pet情報照会ペット情報を抽出して取得
	@PostMapping("/userPetInfoSelect")
	public List<UserPetDTO> userPetInfoSelect(@RequestBody Map<String, String> useremail) {
		String userEmail = useremail.get("useremail");
		return userService.userPetInfoSelect(userEmail);
	}
	
	// ニックネームの変更
    @PutMapping("/nicknameChange")
    public ResponseEntity<String> changeNickname(@RequestBody Map<String, String> userInfo) {
    	String userId = userInfo.get("userid");
    	String newNickname = userInfo.get("newNickname");

        // サービスを使用してニックネームを変更する
        String responseMessage = userService.changeUserNickname(userId, newNickname);

        // 結果による応答を返還
        if (responseMessage.contains("成功")) {
            return ResponseEntity.ok(responseMessage);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
        }
    }
    
    // 自己紹介の内容アップデート
    @PatchMapping("/introduce")
    public ResponseEntity<Void> updateIntroduce(@RequestBody IntroduceRequestDTO introduceRequestDTO) {
        userService.updateUserIntroduce(introduceRequestDTO.getUserid(), introduceRequestDTO.getUserintroduce());
        return ResponseEntity.ok().build();
    }
    
    // 散歩情報を抽出して取得
    @PostMapping("/selectStroll")
    public ResponseEntity<List<StrollDTO>> selectStroll(@RequestBody Map<String, String> userid) {
    	String userId = userid.get("userid");

        if (userId == null || userId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            List<StrollDTO> strolls = userService.getStrollsByUserId(userId);

            if (!strolls.isEmpty()) {
                return ResponseEntity.ok(strolls);
            } else {
                return ResponseEntity.notFound().build(); 
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
