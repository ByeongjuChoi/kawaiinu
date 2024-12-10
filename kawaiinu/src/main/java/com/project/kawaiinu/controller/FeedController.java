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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.project.kawaiinu.entity.CommentsEntity;
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
	// 게시글 등록
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
	// 게시글 삭제
	@DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFeed(@RequestBody Map<String, String> feedInfo) {
		
		String feedId = feedInfo.get("feedid");
		
        feedService.deleteFeed(feedId);
        return ResponseEntity.noContent().build(); // 삭제 성공 시 204 No Content 반환
    }
	
	// MyFeed情報照会
	// 내 피드 정보 조회
	@PostMapping("/myFeedSelect")
	public List<FeedDTO> myFeedSelect(@RequestBody Map<String, String> userInfo) {
		String userId = userInfo.get("userid");
		return feedService.myFeedSelect(userId);
	}
	
	// 全体Feed情報照会
	// 피드 전체 검색
	@GetMapping("/feedSelectAll")
	public List<FeedDTO> feedSelectAll() {
		return feedService.feedSelectAll();
	}
	
	// コメント登録
	// 댓글 작성
	@PostMapping(value = "/commentsave", consumes = "application/json", produces = "application/json")
	public List<CommentsDTO> addCommentToFeed(@RequestBody CommentsDTO commentsDTO) {
		return feedService.addCommentToFeed(commentsDTO);
	}
	
	// コメント Delete
	// 댓글 삭제
	@DeleteMapping("deleteComment")
    public ResponseEntity<Void> deleteComment(@RequestBody CommentsDTO commentsDTO) {
        feedService.deleteComment(commentsDTO);
        return ResponseEntity.noContent().build(); // 삭제 성공 시 204 No Content 반환
    }
	
	// Feed詳細照会
	// 피드 상세조회 (피드와 댓글)
	@PostMapping(value = "/getFeedWithComments", consumes = "application/json", produces = "application/json")
	public FeedWithCommentsDTO getFeedWithComments(@RequestBody FeedRequestDTO feedRequestDTO) {
	    return feedService.getFeedWithComments(feedRequestDTO.getUserid(), feedRequestDTO.getFeedid());
	}
	
	// Feed詳細照会
	// 해당 피드 댓글 조회
	@PostMapping(value = "/getComment", consumes = "application/json", produces = "application/json")
	public List<CommentsDTO> getComment(@RequestBody Map<String, String> feedRequest) {
		String feedId = feedRequest.get("feedid");
	    return feedService.getComment(feedId);
	}
	
	// 피드 공개/비공개 상태 변경
    @PutMapping("/updateFeedStatus")
    public ResponseEntity<?> updateFeedStatus(@RequestBody Map<String, String> userFeedInfo) {
    	
    	String feedId = userFeedInfo.get("feedid");
    	String userid = userFeedInfo.get("userid");
    	// 해당 userId에 맞는 사용자 찾기
        UserEntity user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 피드 찾기
        FeedEntity feedEntity = feedService.findFeedById(feedId);

        // 피드를 작성한 사용자만 상태를 변경할 수 있도록 검증
        if (!feedEntity.getKawaiinuuserfeedid().getUserid().equals(userid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("피드를 작성한 사용자만 상태를 변경할 수 있습니다.");
        }

        // 현재 상태에 따라 공개/비공개 상태 변경
        int newStatus = feedEntity.getFeedstatus() == 1 ? 0 : 1; // 공개 -> 비공개 또는 비공개 -> 공개
        feedEntity.setFeedStatus(newStatus); // 상태 변경
        feedService.saveFeed(feedEntity); // 변경된 피드 저장

        // 상태 변경 완료 응답
        return ResponseEntity.ok("피드 상태가 변경되었습니다.");
    }
	
	//いいね登録、いいねキャンセル
	// 좋아요, 좋아요 취소
	@PostMapping("/toggleLike")
	public ResponseEntity<String> toggleLike(@RequestBody FeedLikeRequestDTO requestDTO) {
	    feedService.toggleLike(requestDTO.getUserid(), requestDTO.getFeedid());
	    return ResponseEntity.ok("좋아요 상태가 변경되었습니다.");
	}

	// いいねカウント照会
	// 좋아요 갯수 조회
	@PostMapping("/getFeedLikeCount")
	public ResponseEntity<Long> getFeedLikeCount(@RequestBody Map<String, String> feedInfo) {
		String feedId = feedInfo.get("feedid");
	    long likeCount = feedService.getFeedLikeCount(feedId);
	    return ResponseEntity.ok(likeCount);
	}
}
