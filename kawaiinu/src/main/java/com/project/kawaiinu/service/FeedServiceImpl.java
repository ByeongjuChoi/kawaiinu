package com.project.kawaiinu.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.kawaiinu.dto.CommentsDTO;
import com.project.kawaiinu.dto.FeedDTO;
import com.project.kawaiinu.dto.FeedWithCommentsDTO;
import com.project.kawaiinu.dto.SelectAllDTO;
import com.project.kawaiinu.entity.CommentsEntity;
import com.project.kawaiinu.entity.FeedEntity;
import com.project.kawaiinu.entity.FeedLikeEntity;
import com.project.kawaiinu.entity.StrollCountEntity;
import com.project.kawaiinu.entity.UserEntity;
import com.project.kawaiinu.repository.CommentRepository;
import com.project.kawaiinu.repository.FeedLikeRepository;
import com.project.kawaiinu.repository.FeedRepository;
import com.project.kawaiinu.repository.StrollCountRepository;
import com.project.kawaiinu.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {
	
	@Autowired
	private FeedRepository feedRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private FeedLikeRepository feedLikeRepository;
	
	@Autowired
    private StrollCountRepository strollCountRepository;
	
	@Autowired
	private AlarmService alarmService;

	// 게시글 저장
	@Override
	public void saveFeed(FeedDTO feedDTO) {
		
		String userId = feedDTO.getUserid();
		UserEntity userEntity = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
		
		// 현재 날짜의 시작 시간과 끝 시간 계산 (오늘 00:00:00 ~ 오늘 23:59:59)
	    LocalDateTime todayStart = LocalDateTime.now().toLocalDate().atStartOfDay();
	    LocalDateTime todayEnd = todayStart.plusDays(1);
	    
	    // 해당 사용자가 오늘 이미 산책한 적이 있는지 확인
	    boolean alreadyStrolled = strollCountRepository.existsByUserAndStrollDateBetween(
	            userEntity, todayStart, todayEnd);
	    
	    // 만약 오늘 산책하지 않았다면, StrollCountEntity를 생성하여 is_strolled 값을 'y'로 설정
	    if (!alreadyStrolled) {
	        StrollCountEntity strollCount = StrollCountEntity.builder()
	                .user(userEntity)  // UserEntity 연결
	                .strollDate(LocalDateTime.now())  // 현재 시간 사용
	                .isStrolled("Y")  // 산책 여부 'y'
	                .build();
	        
	        // StrollCountEntity 저장
	        strollCountRepository.save(strollCount);
	    }
		
		FeedEntity feedinfo = FeedEntity.builder()
				.feedid(feedDTO.getFeedid())
				.feedstatus(1)
				.picture(feedDTO.getPicture())
				.feedcreatedate(LocalDateTime.now())
				.feedlike(0)
				.kawaiinuuserfeedid(userEntity)
				.build();
		feedRepository.save(feedinfo);
	}
	
	// 피드 삭제
	@Override
	@Transactional
	public void deleteFeed(String feedid) {
		// 피드 조회
        FeedEntity feedEntity = feedRepository.findById(feedid)
                .orElseThrow(() -> new RuntimeException("피드를 찾을 수 없습니다."));

        // 해당 피드에 대한 댓글 삭제
        commentRepository.deleteByFeed(feedEntity);

        // 해당 피드에 대한 좋아요 삭제
        feedLikeRepository.deleteByFeed(feedEntity);

        // 피드 삭제
        feedRepository.delete(feedEntity);

        // 피드 삭제 후 StrollCountEntity에 대한 영향이 없도록 하여 'is_strolled'는 그대로 'y'로 유지
        // StrollCountEntity에서 해당 FeedEntity와 연결된 항목들은 삭제하지 않음
	}
	
	// 내 피드 불러오기
	@Override
	public List<FeedDTO> myFeedSelect(String userid) {
		List<Object[]> results = feedRepository.myFeedSelect(userid);
	    List<FeedDTO> feedDTOs = new ArrayList<>();

	    for (Object[] row : results) {
	        FeedDTO feedDTO = new FeedDTO(
	            (String) row[0],  // feedid
	            convertToLocalDateTime((Timestamp) row[1]), // feedcreatedate
	            (Integer) row[2], // feedlike
	            (Integer) row[3],  // feedstatus
	            (String) row[4],  // picture
	            (String) row[5]    // userid
	        );
	        feedDTOs.add(feedDTO);
	    }

	    return feedDTOs;
	}
	
	// 피드 전체 검색
	@Override
	public List<SelectAllDTO> feedSelectAll() {
		
		List<Object[]> results = feedRepository.findAllFeedsWithLikes();
		
		return results.stream()
                 .map(result -> new SelectAllDTO(
                     (String) result[0],  // userId
                     (String) result[1],  // feedId
                     (Timestamp) result[2],  // feedCreateDate
                     (Integer) result[3],  // feedLike
                     (Integer) result[4],  // feedStatus
                     (String) result[5],  // likeUserId
                     (String) result[6]   // likeFeedId
                 ))
                 .collect(Collectors.toList());
	}
	
	// 피드 상세조회 (피드와 댓글)
	@Override
	public FeedWithCommentsDTO getFeedWithComments(String userid, String feedid) {
		// 사용자와 피드 조회
	    UserEntity user = userRepository.findById(userid)
	            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

	    FeedEntity feed = feedRepository.findById(feedid)
	            .orElseThrow(() -> new RuntimeException("피드를 찾을 수 없습니다."));

	    // 피드 정보 DTO로 변환
	    FeedDTO feedDTO = new FeedDTO(
	            feed.getFeedid(),
	            feed.getFeedcreatedate(),
	            feed.getFeedlike(),
	            feed.getFeedstatus(),
	            feed.getPicture(),
	            feed.getKawaiinuuserfeedid().getUserid()
	    );

	    // 해당 피드에 달린 댓글 리스트 조회
	    List<CommentsEntity> comments = commentRepository.findByFeedAndUser(feed, user);

	    // 댓글들을 DTO로 변환
	    List<CommentsDTO> commentsDTOList = comments.stream()
	            .map(c -> new CommentsDTO(
	            		c.getCommentsid(),
	                    c.getFeed().getFeedid(),
	                    c.getUser().getUsernickname(),
	                    c.getUser().getUserid(),
	                    c.getComments(),
	                    c.getCommentscreatedate()
	            ))
	            .collect(Collectors.toList());

	    // 피드와 댓글 정보를 FeedWithCommentsDTO로 묶어서 반환
	    return new FeedWithCommentsDTO(feedDTO, commentsDTOList);
	}
	
	// 댓글 추가
	@Override
	public List<CommentsDTO> addCommentToFeed(CommentsDTO commentsDTO) {
		// 사용자와 피드 조회
        UserEntity user = userRepository.findById(commentsDTO.getUserid())
				.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
		
		FeedEntity feed = feedRepository.findById(commentsDTO.getFeedid())
			        .orElseThrow(() -> new RuntimeException("피드를 찾을 수 없습니다."));
		
		CommentsEntity comment = CommentsEntity.builder()
		    .user(user)
		    .feed(feed)
		    .comments(commentsDTO.getComments())
		    .commentscreatedate(LocalDateTime.now())
		    .build();
		
		// 댓글을 저장
		commentRepository.save(comment);
		
		// 피드에 댓글 추가 후 피드 저장
		feed.addComment(comment);
		// 피드를 다시 저장해서 댓글 목록을 업데이트
		feedRepository.save(feed);  
		
		// 본인의 피드에 본인이 쓴 댓글은 알람테이블에 집어넣지 않는다.
		if(!user.getUserid().equals(feed.getKawaiinuuserfeedid().getUserid())) {
			alarmService.createLikeOrCommentAlarm(user.getUserid(), feed, 'C');			
		}
		
		// 피드에 달린 모든 댓글을 조회
	    List<CommentsEntity> allComments = commentRepository.findByFeed(feed);

	    // 댓글들을 DTO로 변환하여 리스트로 반환
	    List<CommentsDTO> commentsDTOList = allComments.stream()
	            .map(c -> new CommentsDTO(
	            		c.getCommentsid(),
	                    c.getFeed().getFeedid(),
	                    c.getUser().getUsernickname(),  // 유저 닉네임 추가
	                    c.getUser().getUserid(),
	                    c.getComments(),
	                    c.getCommentscreatedate()
	            ))
	            .collect(Collectors.toList());
	    return commentsDTOList;
	}
	
	// 댓글 수정
	@Override
	public CommentsDTO updateComment(CommentsDTO commentsDTO) {
	    // 댓글 조회
	    CommentsEntity comment = commentRepository.findById(commentsDTO.getCommentsid())
	            .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

	    // 댓글 내용 업데이트
	    comment.setComments(commentsDTO.getComments());
	    comment.setCommentscreatedate(LocalDateTime.now()); // 수정 시간 업데이트

	    // 댓글 저장
	    commentRepository.save(comment);

	    // 업데이트된 댓글을 DTO로 변환하여 반환
	    return new CommentsDTO(
	            comment.getCommentsid(),
	            comment.getFeed().getFeedid(),
	            comment.getUser().getUsernickname(),
	            comment.getUser().getUserid(),
	            comment.getComments(),
	            comment.getCommentscreatedate()
	    );
	}
	
	// 댓글 조회
	@Override
	public List<CommentsDTO> getComment(String feedId) {
		FeedEntity feed = feedRepository.findById(feedId)
		        .orElseThrow(() -> new RuntimeException("피드를 찾을 수 없습니다."));
		
		List<CommentsEntity> commentEntity = commentRepository.findByFeed(feed);
		
		return commentEntity.stream()
				.map(comment -> CommentsDTO.builder()
				        .commentsid(comment.getCommentsid()) 
				        .userid(comment.getUser().getUserid()) 
				        .feedid(comment.getFeed().getFeedid()) 
				        .comments(comment.getComments())
				        .usernickname(comment.getUser().getUsernickname())
				        .commentsCreatedDate(comment.getCommentscreatedate())
				        .build()
				    )
				    .collect(Collectors.toList());
	}

	// 댓글 삭제
	@Override
	@Transactional
	public void deleteComment(CommentsDTO commentsDTO) {		
        CommentsEntity commentsEntity = commentRepository.findById(commentsDTO.getCommentsid())
        		.orElseThrow(() -> new RuntimeException("코멘트를 찾을 수 없습니다."));

        commentRepository.delete(commentsEntity);
	}
	
	// 좋아요 및 좋아요 취소
	@Override
	@Transactional
	public void toggleLike(String userid, String feedid) {
	    // 사용자와 피드 조회
	    UserEntity user = userRepository.findById(userid)
	            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
	    FeedEntity feed = feedRepository.findById(feedid)
	            .orElseThrow(() -> new RuntimeException("피드를 찾을 수 없습니다."));

	    // 사용자가 해당 피드에 좋아요를 눌렀는지 확인
	    boolean alreadyLiked = feedLikeRepository.existsByUserAndFeed(user, feed);

	    if (alreadyLiked) {
	        // 좋아요 취소
	        feedLikeRepository.deleteByUserAndFeed(user, feed);
	        feed.setFeedlike(feed.getFeedlike() - 1);  // 피드의 좋아요 수 감소
	    } else {
	        // 좋아요 추가
	        FeedLikeEntity feedLike = new FeedLikeEntity();
	        feedLike.setUser(user);
	        feedLike.setFeed(feed);
	        feedLike.setLikedate(LocalDateTime.now());
	        feedLikeRepository.save(feedLike);
	        feed.setFeedlike(feed.getFeedlike() + 1);  // 피드의 좋아요 수 증가
	    }

	    // 피드 업데이트
	    feedRepository.save(feed);
	    
	    // 본인의 피드에 본인이 쓴 댓글은 알람테이블에 집어넣지 않는다.
 		if(!user.getUserid().equals(feed.getKawaiinuuserfeedid().getUserid())) {
 			alarmService.createLikeOrCommentAlarm(user.getUserid(), feed, 'L');			
 		}
	}
	
	// 좋아요 갯수 조회
	@Override
	public long getFeedLikeCount(String feedid) {
	    FeedEntity feed = feedRepository.findById(feedid)
	            .orElseThrow(() -> new RuntimeException("피드를 찾을 수 없습니다."));
	    return feedLikeRepository.countByFeed(feed);  // 피드에 달린 총 좋아요 수 반환
	}
	
	
	// 피드 ID로 피드 조회
	@Override
    public FeedEntity findFeedById(String feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("피드를 찾을 수 없습니다."));
    }

    // 피드 상태 저장
	@Override
    public void saveFeed(FeedEntity feedEntity) {
        feedRepository.save(feedEntity); // 피드 저장
    }
	
	// Timestamp를 LocalDateTime으로 변환하는 유틸리티 메소드
	private LocalDateTime convertToLocalDateTime(Timestamp timestamp) {
	    return timestamp.toLocalDateTime();  // Timestamp를 LocalDateTime으로 변환
	}
}
