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
        // 닉네임 중복 검사
        if (!nickDupCheck(userDTO.getUsernickname())) {
            // 유저 정보 저장
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
        // 이메일로 유저 찾기
        UserEntity userEntity = userRepository.findByUseremail(userPetDTO.getUseremail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userPetDTO.getUseremail()));

        // 펫 정보 저장
        UserPetEntity userPetEntity = UserPetEntity.builder()
                .kawaiinuuserid(userEntity) // 유저의 ID 설정
                .petname(userPetDTO.getPetname())
                .petbreed(userPetDTO.getPetbreed())
                .petage(userPetDTO.getPetage())
                .petgender(userPetDTO.getPetgender())
                .petsnack(userPetDTO.getPetsnack())
                .petweight(userPetDTO.getPetweight())
                .build();
        
        userPetRepository.save(userPetEntity);
    }
	
	// 닉네임 변경 메소드
    @Transactional
    public String changeUserNickname(String userId, String newNickname) {
        // 중복 체크
        if (nickDupCheck(newNickname)) {
            return "닉네임이 이미 존재합니다.";
        }

        // 사용자 조회
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 닉네임 변경
        user.setUsernickname(newNickname);

        // 저장
        userRepository.save(user);

        return "닉네임이 성공적으로 변경되었습니다.";
    }
	
	// 닉네임 중복 검사
	@Override
    public boolean nickDupCheck(String nickname) {
        return userRepository.existsByUsernickname(nickname);
    }
	
	// 이메일 중복 검사 메서드
	@Override
    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByUseremail(email);
    }

	// 유저 조회
	@Override
	@Transactional
	public List<UserDTO> userInfoSelect(String useremail) {
		// JPQL로 UserEntity 리스트를 조회
        List<UserEntity> userEntities = userRepository.userInfoSelect(useremail);

        // UserEntity 리스트를 UserDTO 리스트로 변환
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
	
	// 펫 조회
	@Override
	@Transactional
	public List<UserPetDTO> userPetInfoSelect(String useremail) {
		// JPQL로 UserPetEntity 리스트를 조회
        List<UserPetEntity> userPetEntities = userPetRepository.userPetInfoSelect(useremail);

        // UserPetEntity 리스트를 UserPetDTO 리스트로 변환
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

	// 자기소개글 업데이트
	@Override
	@Transactional
	public void updateUserIntroduce(String userid, String userintroduce) {
		
		UserEntity userEntity = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        userEntity.setUserintroduce(userintroduce); // 자기소개글 업데이트
        userRepository.save(userEntity);           // 변경 사항 저장
	}
	
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
