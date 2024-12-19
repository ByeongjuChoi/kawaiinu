package com.project.kawaiinu.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "kawaiinu_user")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class UserEntity {
	@Id
    @Column(name = "userid", nullable = false, unique = true, length = 50)
	@NotBlank(message = "User IDは必ず入力して下さい。.")
	private String userid;
	
	@Column(name = "useremail", nullable = false)
	private String useremail;
	
	@Column(name = "usernickname", nullable = false)
	private String usernickname;
	
	@Column(name = "userage")
	private String userage;
	
	@Column(name = "usergender")
	private String usergender;
	
	@Column(name = "userintroduce", nullable = true)
	@Size(max = 30, message = "自己紹介は最大30文字まで可能です。")
	private String userintroduce;
	
	// ユーザーの生成日(自動に生成される)
	@CreationTimestamp
	@Column(name = "usercreatedate", updatable = false, nullable = false)
	private LocalDateTime usercreatedate;
	
	@JsonIgnore
    @OneToMany(mappedBy = "kawaiinuuserid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPetEntity> userpets = new ArrayList<>();
	
	@JsonIgnore
    @OneToMany(mappedBy = "kawaiinuuserfeedid")
    private List<FeedEntity> userfeeds = new ArrayList<>();
	
	// ペット追加メソッド
    public void addPet(UserPetEntity pet) {
        this.userpets.add(pet);
        pet.setKawaiinuuserid(this);
    }

    // ペット削除メソッド
    public void removePet(UserPetEntity pet) {
        this.userpets.remove(pet);
        pet.setKawaiinuuserid(null);
    }
	
}