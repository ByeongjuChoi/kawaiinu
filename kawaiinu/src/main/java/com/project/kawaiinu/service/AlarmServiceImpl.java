package com.project.kawaiinu.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.kawaiinu.dto.AlarmDTO;
import com.project.kawaiinu.dto.CommentsDTO;
import com.project.kawaiinu.dto.FeedDTO;
import com.project.kawaiinu.dto.FeedWithCommentsDTO;
import com.project.kawaiinu.dto.SelectAllDTO;
import com.project.kawaiinu.entity.AlarmEntity;
import com.project.kawaiinu.entity.CommentsEntity;
import com.project.kawaiinu.entity.FeedEntity;
import com.project.kawaiinu.entity.UserEntity;
import com.project.kawaiinu.repository.AlarmRepository;
import com.project.kawaiinu.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {
	
	@Autowired
	private AlarmRepository alarmRepository;
	
	@Autowired
	private UserRepository userRepository;

	// 알람 생성
	@Override
	@Transactional
	public void createLikeOrCommentAlarm(String otherUserid, FeedEntity feed, char LikeOrComment) {
		AlarmEntity alarm = AlarmEntity.builder()
				.user(feed.getKawaiinuuserfeedid())
				.feed(feed)
				.readYN('N') 
				.likeOrComment(LikeOrComment)	// 던져주는 값 C or L
				.otherUserId(otherUserid)
				.build();
		alarmRepository.save(alarm);
	}
	
	// 알람조회
	@Override
	public List<AlarmDTO> mySelectAlarm(String userid) {
		// 사용자와 피드 조회
	    UserEntity user = userRepository.findById(userid)
	            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

	    // 해당 유저의 알람을 불러옴
	    List<Object[]> results = alarmRepository.mySelectAlarm(userid);
	    
		return results.stream()
                 .map(result -> new AlarmDTO(
                     (Long) result[0],			// alarmId
                 	 (String) result[1],		// 유저 아이디
                 	 (String) result[2],		// 유저 닉네임
                 	 (String) result[3],		// 댓글 or 좋아요 실행한 사용자
                 	 (String) result[4],		// 댓글 or 좋아요 실행한 사용자의 닉네임
                 	 (Timestamp) result[5],	// 생성날짜
                 	 (char) result[6],			// L: 좋아요, C: 댓글
                 	 (String) result[7],		// 피드 아이디
                 	 (char) result[8]			// Y: 읽음, N: 읽지않음
                 ))
                 .collect(Collectors.toList());
	}
	
	// 알람 갯수 조회
	@Override
	public int alarmCount(String userid) {
	    int alarmCnt = alarmRepository.alarmCount(userid);
		return alarmCnt;
	}
	
	// 알람 확인
	@Override
	@Transactional
	public void alarmCheck(int alarmid) {
		alarmRepository.alarmCheck(alarmid);
	}
}
