package com.project.kawaiinu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPetDTO {
	private Long   petid;
    private String petname;
    private String petbreed;
    private String petage;
    private String petgender;
    private String petsnack;
    private Double petweight;
    private String useremail; 
    private String kawaiinuuserid;
    
}