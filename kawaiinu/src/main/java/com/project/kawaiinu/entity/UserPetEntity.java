package com.project.kawaiinu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "kawaiinu_user_pet")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class UserPetEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "petid", nullable = false)
	private Long petid;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false) // 외래 키를 가리키는 칼럼명 지정
    private UserEntity kawaiinuuserid;
    
	@Column(name = "petname")
	private String petname;
	
	@Column(name = "petbreed")
	private String petbreed;
	
	@Column(name = "petage")
	private String petage;
	
	@Column(name = "petsnack")
	private String petsnack;
	
	@Column(name = "petgender")
	private String petgender;
	
	@Column(name = "petweight")
	private Double petweight;
}
