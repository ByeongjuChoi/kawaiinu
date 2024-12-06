package com.project.kawaiinu.service;

import java.util.List;

import com.project.kawaiinu.dto.CommentsDTO;
import com.project.kawaiinu.dto.FeedDTO;
import com.project.kawaiinu.dto.FeedWithCommentsDTO;
import com.project.kawaiinu.entity.FeedEntity;

public interface FeedService {

	public void saveFeed(FeedDTO feedDTO);
	public List<CommentsDTO> addCommentToFeed(CommentsDTO commentsDTO);
	public List<FeedDTO> myFeedSelect(String userid);
	public List<FeedDTO> feedSelectAll();
	public FeedWithCommentsDTO getFeedWithComments(String userid, Long feedid);
	public void toggleLike(String userid, Long feedid);
	public long getFeedLikeCount(Long feedid);
	public void deleteFeed(Long feedid);
	public void deleteComment(CommentsDTO commentsDTO);
	
	public void saveFeed(FeedEntity feedEntity);
	public FeedEntity findFeedById(Long feedId);
}
