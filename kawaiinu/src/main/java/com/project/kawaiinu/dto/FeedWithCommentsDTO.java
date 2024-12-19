package com.project.kawaiinu.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedWithCommentsDTO {
	private FeedDTO feed;                    // ポスト情報
    private List<CommentsDTO> comments;      // 該当するポストに作成されているリプライ
}
