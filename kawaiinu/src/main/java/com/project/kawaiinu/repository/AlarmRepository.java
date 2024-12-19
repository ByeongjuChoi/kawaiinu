package com.project.kawaiinu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.project.kawaiinu.entity.AlarmEntity;

public interface AlarmRepository extends JpaRepository<AlarmEntity, Long>{
	// 該当するユーザーの全アラムの内容を抽出して取得
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
				 ORDER BY alarmid desc
	        """, nativeQuery = true)
    List<Object[]> mySelectAlarm(String userid);
    
	// 該当するユーザーの全アラムの情報を抽出して取得
	@Query(value = """
				SELECT count(*)
			      FROM kawaiinu_alarm a
				 WHERE a.readyn = 'N'
				   AND a.userid = :userid
	        """, nativeQuery = true)
    int alarmCount(String userid);
	
	// 該当するユーザーのアラムを確認
	@Modifying
	@Query(value = """
				UPDATE kawaiinu_alarm
				   SET readyn = 'Y'
				 WHERE alarmid = :alarmid
	        """, nativeQuery = true)
    void alarmCheck(int alarmid);
	
	// 該当するユーザーの全アラムを確認
	@Modifying
	@Query(value = """
				UPDATE kawaiinu_alarm
				   SET readyn = 'Y'
				 WHERE userid = :userid
				   AND readyn = 'N'
	        """, nativeQuery = true)
    void alarmCheckAll(String userid);
}
