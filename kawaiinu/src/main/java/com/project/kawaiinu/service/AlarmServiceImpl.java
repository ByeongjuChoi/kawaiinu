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

	// アラム生成
	@Override
	@Transactional
	public void createLikeOrCommentAlarm(String otherUserid, FeedEntity feed, char LikeOrComment) {
		AlarmEntity alarm = AlarmEntity.builder()
				.user(feed.getKawaiinuuserfeedid())
				.feed(feed)
				.readYN('N') 
				.likeOrComment(LikeOrComment)	// 伝達する値C or L
				.otherUserId(otherUserid)
				.build();
		alarmRepository.save(alarm);
	}
	
	// アラムの情報を抽出して取得
	@Override
	public List<AlarmDTO> mySelectAlarm(String userid) {
		// ユーザーとポストの情報を抽出して取得
	    UserEntity user = userRepository.findById(userid)
	            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません。"));

	    // 該当するユーザーのアラムの情報を抽出して取得
	    List<Object[]> results = alarmRepository.mySelectAlarm(userid);
	    
		return results.stream()
                 .map(result -> new AlarmDTO(
                     (Long) result[0],			// alarmId
                 	 (String) result[1],		// ユーザーID
                 	 (String) result[2],		// ユーザーニックネーム
                 	 (String) result[3],		// リプライ or いいねをしたユーザー
                 	 (String) result[4],		// リプライ or いいねをしたユーザーのニックネーム
                 	 (Timestamp) result[5],		// 生成日
                 	 (char) result[6],			// L:いいね、C:リプライ
                 	 (String) result[7],		// ポストID
                 	 (char) result[8]			// Y：読んだ、N：読んでいない
                 ))
                 .collect(Collectors.toList());
	}
	
	// アラムの個数の情報を抽出して取得ユーザー
	@Override
	public int alarmCount(String userid) {
	    int alarmCnt = alarmRepository.alarmCount(userid);
		return alarmCnt;
	}
	
	// アラムチェック
	@Override
	@Transactional
	public void alarmCheck(int alarmid) {
		alarmRepository.alarmCheck(alarmid);
	}
	
	// 全アラムチェック
	@Override
	@Transactional
	public void alarmCheckAll(String userid) {
		alarmRepository.alarmCheckAll(userid);
	}
}
