package com.project.kawaiinu.dto;

import java.time.LocalDateTime;

import com.project.kawaiinu.entity.FeedEntity;
import com.project.kawaiinu.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrollDTO {
    private Long strollId;
    private String userid;
    private String feedid;  
    private LocalDateTime strollDate;
    private String isStrolled;  // 散歩の存否(Y or N)
}
