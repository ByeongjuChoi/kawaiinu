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
    private String feedid;  // 해당 StrollCountEntity가 연관된 FeedEntity
    private LocalDateTime strollDate;  // 산책 날짜
    private String isStrolled;  // 산책 여부 ('y' or 'n')
}
