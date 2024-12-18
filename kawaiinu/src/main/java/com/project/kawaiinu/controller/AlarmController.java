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
	
	// 알람 정보 조회
	@PostMapping("/mySelectAlarm")
	public List<AlarmDTO> mySelectAlarm(@RequestBody Map<String, String> userInfo) {
		String userId = userInfo.get("userid");
		return alarmService.mySelectAlarm(userId);
	}

	// 알람 갯수 조회
	@PostMapping("/alarmCount")
	public int alarmCount(@RequestBody Map<String, String> userInfo) {
		String userId = userInfo.get("userid");
		return alarmService.alarmCount(userId);
	}
	
	// 알람 확인
	@PostMapping("/alarmCheck")
	public void alarmCheck(@RequestBody Map<String, Integer> alarmInfo) {
		int alarmid = alarmInfo.get("alarmid");
		alarmService.alarmCheck(alarmid);
	}
}
