package com.project.kawaiinu.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.kawaiinu.dto.StrollDTO;
import com.project.kawaiinu.dto.UserDTO;
import com.project.kawaiinu.dto.UserPetDTO;
import com.project.kawaiinu.entity.StrollCountEntity;
import com.project.kawaiinu.entity.UserEntity;
import com.project.kawaiinu.entity.UserPetEntity;
import com.project.kawaiinu.repository.StrollCountRepository;
import com.project.kawaiinu.repository.UserPetRepository;
import com.project.kawaiinu.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserPetRepository userPetRepository;
	
	@Autowired
    private StrollCountRepository strollCountRepository;
	
	@Override
	@Transactional
    public void saveUser(UserDTO userDTO) {
        // ユーザーニックネームの重複検査
        if (!nickDupCheck(userDTO.getUsernickname())) {
            // ユーザーの情報を追加
            UserEntity userEntity = UserEntity.builder()
            		.userid(userDTO.getUserid())
                    .useremail(userDTO.getUseremail())
                    .usernickname(userDTO.getUsernickname())
                    .userage(userDTO.getUserage())
                    .usergender(userDTO.getUsergender())
                    .build();
            userRepository.save(userEntity);
        }
    }
	
	@Transactional
    public void saveUserPet(UserPetDTO userPetDTO) {
        // メールアドレスでユーザー情報を抽出して取得
        UserEntity userEntity = userRepository.findByUseremail(userPetDTO.getUseremail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userPetDTO.getUseremail()));

        // ペット情報追加
        UserPetEntity userPetEntity = UserPetEntity.builder()
                .kawaiinuuserid(userEntity) // ユーザーID設定
                .petname(userPetDTO.getPetname())
                .petbreed(userPetDTO.getPetbreed())
                .petage(userPetDTO.getPetage())
                .petgender(userPetDTO.getPetgender())
                .petsnack(userPetDTO.getPetsnack())
                .petweight(userPetDTO.getPetweight())
                .build();
        
        userPetRepository.save(userPetEntity);
    }
	
	// ニックネームの変更メソッド
    @Transactional
    public String changeUserNickname(String userId, String newNickname) {
        // 重複検査
        if (nickDupCheck(newNickname)) {
            return "存在するニックネームです。";
        }

        // ユーザー情報を抽出して取得
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません。"));

        // ニックネーム変更
        user.setUsernickname(newNickname);

        userRepository.save(user);

        return "ニックネームの変更が完了されました。";
    }
	
	// ニックネームの重複検査
	@Override
    public boolean nickDupCheck(String nickname) {
        return userRepository.existsByUsernickname(nickname);
    }
	
	// メールアドレスの重複検査
	@Override
    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByUseremail(email);
    }

	// ユーザー情報を抽出して取得
	@Override
	@Transactional
	public List<UserDTO> userInfoSelect(String useremail) {
		// JPQLでUserEntityの情報を抽出して取得
        List<UserEntity> userEntities = userRepository.userInfoSelect(useremail);

        // UserEntityリストをUserDTOリストに変換
        return userEntities.stream()
                .map(userEntity -> UserDTO.builder()
                        .userid(userEntity.getUserid())
                        .usernickname(userEntity.getUsernickname())
                        .useremail(userEntity.getUseremail())
                        .userage(userEntity.getUserage())
                        .usergender(userEntity.getUsergender())
                        .userintroduce(userEntity.getUserintroduce())
                        .build())
                .toList();
	}
	
	// ペット情報を抽出して取得
	@Override
	@Transactional
	public List<UserPetDTO> userPetInfoSelect(String useremail) {
		// JPQLでUserPetEntityリストの情報を抽出して取得
        List<UserPetEntity> userPetEntities = userPetRepository.userPetInfoSelect(useremail);

        // UserPetEntityリストをUserPetDTOリストに変換
        return userPetEntities.stream()
                .map(petEntity -> UserPetDTO.builder()
                        .petid(petEntity.getPetid())
                        .petname(petEntity.getPetname())
                        .petbreed(petEntity.getPetbreed())
                        .petage(petEntity.getPetage())
                        .petgender(petEntity.getPetgender())
                        .petsnack(petEntity.getPetsnack())
                        .petweight(petEntity.getPetweight())
                        .kawaiinuuserid(petEntity.getKawaiinuuserid().getUserid())
                        .build())
                .toList();
	}

	// 自己紹介のアップデート
	@Override
	@Transactional
	public void updateUserIntroduce(String userid, String userintroduce) {
		
		UserEntity userEntity = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません。"));
        userEntity.setUserintroduce(userintroduce);	// 自己紹介のアップデート
        userRepository.save(userEntity);           	// 変更事項をアップデート
	}
	
	// 散歩情報を抽出して取得
	@Override
	@Transactional
	public List<StrollDTO> getStrollsByUserId(String userId) {
		
        List<StrollCountEntity> strollEntities = strollCountRepository.findByUser_Userid(userId);
        
        return strollEntities.stream()
                .map(strollEntity -> StrollDTO.builder()
                        .userid(strollEntity.getUser() != null ? strollEntity.getUser().getUserid() : "No User")
                        .strollId(strollEntity.getStrollId())
                        .strollDate(strollEntity.getStrollDate())
                        .isStrolled(strollEntity.getIsStrolled())
                        .feedid(strollEntity.getFeed() != null ? strollEntity.getFeed().getFeedid() : null)
                        .build())
                .toList();
    }

}
