package com.project.kawaiinu.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedWithCommentsDTO {
	private FeedDTO feed;                    // 피드 정보
    private List<CommentsDTO> comments;      // 해당 피드에 달린 댓글들
}
