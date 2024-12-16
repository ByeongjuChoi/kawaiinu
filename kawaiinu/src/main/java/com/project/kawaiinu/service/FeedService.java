package com.project.kawaiinu.service;

import java.util.List;

import com.project.kawaiinu.dto.CommentsDTO;
import com.project.kawaiinu.dto.FeedDTO;
import com.project.kawaiinu.dto.FeedWithCommentsDTO;
import com.project.kawaiinu.dto.SelectAllDTO;
import com.project.kawaiinu.entity.FeedEntity;

public interface FeedService {

	public void saveFeed(FeedDTO feedDTO);
	public List<CommentsDTO> addCommentToFeed(CommentsDTO commentsDTO);
	public List<FeedDTO> myFeedSelect(String userid);
	public List<SelectAllDTO> feedSelectAll();
	public FeedWithCommentsDTO getFeedWithComments(String userid, String feedid);
	public void toggleLike(String userid, String feedid);
	public long getFeedLikeCount(String feedid);
	public void deleteFeed(String feedid);
	public void deleteComment(CommentsDTO commentsDTO);
	
	public void saveFeed(FeedEntity feedEntity);
	public FeedEntity findFeedById(String feedId);
	public List<CommentsDTO> getComment(String feedId);
	
	public CommentsDTO updateComment(CommentsDTO commentsDTO);
}
