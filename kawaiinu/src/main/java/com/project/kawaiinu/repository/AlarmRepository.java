package com.project.kawaiinu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.project.kawaiinu.entity.AlarmEntity;

public interface AlarmRepository extends JpaRepository<AlarmEntity, Long>{
	// 해당유저의 알람 전체 불러오기
	@Query(value = """
				SELECT
						a.alarmid,
						a.userid,
						(select usernickname from kawaiinu_user u where u.userid = a.userid) as usernickname,
						a.other_userid,
						(select usernickname from kawaiinu_user u where u.userid = a.other_userid) as otherusernickname,
						a.create_date,
						a.like_or_comment,
						a.feedid,
						a.readyn
			      FROM kawaiinu_alarm a
				 WHERE a.readyn = 'N'
				   AND a.userid = :userid
	        """, nativeQuery = true)
    List<Object[]> mySelectAlarm(String userid);
    
	// 해당유저의 알람 전체 불러오기
	@Query(value = """
				SELECT count(*)
			      FROM kawaiinu_alarm a
				 WHERE a.readyn = 'N'
				   AND a.userid = :userid
	        """, nativeQuery = true)
    int alarmCount(String userid);
	
	// 해당유저의 알람 확인
	@Modifying
	@Query(value = """
				UPDATE kawaiinu_alarm
				   SET readyn = 'Y'
				 WHERE alarmid = :alarmid
	        """, nativeQuery = true)
    void alarmCheck(int alarmid);
}
