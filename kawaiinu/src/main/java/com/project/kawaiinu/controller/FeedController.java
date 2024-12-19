package com.project.kawaiinu.controller;

import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.kawaiinu.dto.CommentsDTO;
import com.project.kawaiinu.dto.FeedDTO;
import com.project.kawaiinu.dto.FeedLikeRequestDTO;
import com.project.kawaiinu.dto.FeedRequestDTO;
import com.project.kawaiinu.dto.FeedWithCommentsDTO;
import com.project.kawaiinu.dto.SelectAllDTO;
import com.project.kawaiinu.entity.FeedEntity;
import com.project.kawaiinu.entity.UserEntity;
import com.project.kawaiinu.repository.UserRepository;
import com.project.kawaiinu.service.FeedService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/feed")
@RestController
@RequiredArgsConstructor
public class FeedController {

	@Autowired
	private FeedService feedService;
	
	@Autowired
	private UserRepository userRepository;
	
	// Feed登録
	// ポスト登録
	@PostMapping(value = "/feedsave"
			, consumes = "application/json"
			)
	public ResponseEntity feedSave(@Valid @RequestBody FeedDTO feedDTO, Errors error, Model model) throws ParseException {
		if (error.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getAllErrors());
		}
		feedService.saveFeed(feedDTO);
		
	    return ResponseEntity.ok(feedDTO);
	}
	
	// Feed Delete
	// ポストデリート
	@DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFeed(@RequestBody Map<String, String> feedInfo) {
		
		String feedId = feedInfo.get("feedid");
		
        feedService.deleteFeed(feedId);
        return ResponseEntity.noContent().build(); // 삭제 성공 시 204 No Content 반환
    }
	
	// MyFeed情報照会
	// ユーザーのポスト情報を抽出して取得
	@PostMapping("/myFeedSelect")
	public List<FeedDTO> myFeedSelect(@RequestBody Map<String, String> userInfo) {
		String userId = userInfo.get("userid");
		return feedService.myFeedSelect(userId);
	}
	
	// 全体Feed情報照会
	// 全ポストの情報を抽出して取得
	@GetMapping("/feedSelectAll")
	public List<SelectAllDTO> feedSelectAll() {
		return feedService.feedSelectAll();
	}
	
	// リプライ登録(追加)
	@PostMapping(value = "/commentsave", consumes = "application/json", produces = "application/json")
	public List<CommentsDTO> addCommentToFeed(@RequestBody CommentsDTO commentsDTO) {
		return feedService.addCommentToFeed(commentsDTO);
	}
	
	// リプライアップデート
	@PutMapping(value = "/commentupdate", consumes = "application/json", produces = "application/json")
	public CommentsDTO updateComment(@RequestBody CommentsDTO commentsDTO) {
	    return feedService.updateComment(commentsDTO);
	}
	
	// リプライデリート
	@DeleteMapping("deleteComment")
    public ResponseEntity<Void> deleteComment(@RequestBody CommentsDTO commentsDTO) {
        feedService.deleteComment(commentsDTO);
        return ResponseEntity.noContent().build();
    }
	
	// Feed詳細照会
	// ポストの詳細情報を抽出して取得(ポストとリプライ)
	@PostMapping(value = "/getFeedWithComments", consumes = "application/json", produces = "application/json")
	public FeedWithCommentsDTO getFeedWithComments(@RequestBody FeedRequestDTO feedRequestDTO) {
	    return feedService.getFeedWithComments(feedRequestDTO.getUserid(), feedRequestDTO.getFeedid());
	}
	
	// Feed詳細照会
	// 該当するポストのリプライを抽出して取得
	@PostMapping(value = "/getComment", consumes = "application/json", produces = "application/json")
	public List<CommentsDTO> getComment(@RequestBody Map<String, String> feedRequest) {
		String feedId = feedRequest.get("feedid");
	    return feedService.getComment(feedId);
	}
	
	// ポストの公開/非公開の状態変更
    @PutMapping("/updateFeedStatus")
    public ResponseEntity<?> updateFeedStatus(@RequestBody Map<String, String> userFeedInfo) {
    	
    	String feedId = userFeedInfo.get("feedid");
    	String userid = userFeedInfo.get("userid");
    	// 該当するユーザーidに当たるユーザーの情報を抽出して取得
        UserEntity user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません。"));

        // ポストの情報を抽出して取得
        FeedEntity feedEntity = feedService.findFeedById(feedId);

        // ポストを作成したユーザーのみポストの状態の変更が出来るように検証
        if (!feedEntity.getKawaiinuuserfeedid().getUserid().equals(userid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("ポストを作成したユーザーのみ修正が可能です。");
        }

        // 現在状態によって公開/非公開の状態変更
        int newStatus = feedEntity.getFeedstatus() == 1 ? 0 : 1; // 公開→非公開/公開→公開
        feedEntity.setFeedStatus(newStatus); // 状態変更
        feedService.saveFeed(feedEntity); // 変更されたポストをアップデート

        // 状態変更完了の応答
        return ResponseEntity.ok("ポストの状態が変更されました。");
    }
	
	//いいね登録、いいねキャンセル
	@PostMapping("/toggleLike")
	public ResponseEntity<String> toggleLike(@RequestBody FeedLikeRequestDTO requestDTO) {
	    feedService.toggleLike(requestDTO.getUserid(), requestDTO.getFeedid());
	    return ResponseEntity.ok("いいねの状態が変更されました。");
	}

	// いいねカウント照会
	// いいねの個数を抽出して取得
	@PostMapping("/getFeedLikeCount")
	public ResponseEntity<Long> getFeedLikeCount(@RequestBody Map<String, String> feedInfo) {
		String feedId = feedInfo.get("feedid");
	    long likeCount = feedService.getFeedLikeCount(feedId);
	    return ResponseEntity.ok(likeCount);
	}
}
