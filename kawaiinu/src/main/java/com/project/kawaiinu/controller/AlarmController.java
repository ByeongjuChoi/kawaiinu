package com.project.kawaiinu.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.kawaiinu.dto.AlarmDTO;
import com.project.kawaiinu.service.AlarmService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/alarm")
@RestController
@RequiredArgsConstructor
public class AlarmController {

	@Autowired
	private AlarmService alarmService;
	
	// アラーム情報を抽出して取得
	@PostMapping("/mySelectAlarm")
	public List<AlarmDTO> mySelectAlarm(@RequestBody Map<String, String> userInfo) {
		String userId = userInfo.get("userid");
		return alarmService.mySelectAlarm(userId);
	}

	// アラーム個数の情報を抽出して取得
	@PostMapping("/alarmCount")
	public int alarmCount(@RequestBody Map<String, String> userInfo) {
		String userId = userInfo.get("userid");
		return alarmService.alarmCount(userId);
	}
	
	// アラーム確認
	@PostMapping("/alarmCheck")
	public void alarmCheck(@RequestBody Map<String, Integer> alarmInfo) {
		int alarmid = alarmInfo.get("alarmid");
		alarmService.alarmCheck(alarmid);
	}
	
	// アラーム全体確認
	@PostMapping("/alarmCheckAll")
	public void alarmCheckAll(@RequestBody Map<String, String> alarmInfo) {
		String userid = alarmInfo.get("userid");
		alarmService.alarmCheckAll(userid);
	}
}
