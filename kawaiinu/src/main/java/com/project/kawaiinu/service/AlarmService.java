package com.project.kawaiinu.service;

import java.util.List;

import com.project.kawaiinu.dto.AlarmDTO;
import com.project.kawaiinu.entity.FeedEntity;

public interface AlarmService {
	void createLikeOrCommentAlarm(String otherUserid, FeedEntity feed, char LikeOrComment);
	List<AlarmDTO> mySelectAlarm(String userid);
	int alarmCount(String userid);
	void alarmCheck(int alarmid);
	void alarmCheckAll(String userid);
}
