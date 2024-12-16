package com.project.kawaiinu.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectAllDTO {
	private String feedId;
    private String userId;
    private String picture;
    private Integer feedLike;
    private Timestamp feedCreateDate;
    private Integer feedStatus;
    private String likeUserId; // 새 필드 추가
    private String likeFeedId; // 새 필드 추가
    
    // 생성자 추가
    public SelectAllDTO(String userId, String feedId, Timestamp feedCreateDate, Integer feedLike, Integer feedStatus, String likeUserId, String likeFeedId) {
        this.userId = userId;
        this.feedId = feedId;
        this.feedCreateDate = feedCreateDate;
        this.feedLike = feedLike;
        this.feedStatus = feedStatus;
        this.likeUserId = likeUserId;
        this.likeFeedId = likeFeedId;
    }
    
}