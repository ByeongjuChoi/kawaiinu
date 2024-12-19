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

	// ポスト登録
	@Override
	public void saveFeed(FeedDTO feedDTO) {
		
		String userId = feedDTO.getUserid();
		UserEntity userEntity = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("ユーザーが見つかりません。"));
		
		// 現在日付の始まる時間と終わる時間を計算(当日00:00:00~当日23:59:59)
	    LocalDateTime todayStart = LocalDateTime.now().toLocalDate().atStartOfDay();
	    LocalDateTime todayEnd = todayStart.plusDays(1);
	    
	    // 該当するユーザーがの当日散歩の存否
	    boolean alreadyStrolled = strollCountRepository.existsByUserAndStrollDateBetween(
	            userEntity, todayStart, todayEnd);
	    
	    // 散歩をしていないならStrollCountEntityを生成してis_strolledの値をyに設定
	    if (!alreadyStrolled) {
	        StrollCountEntity strollCount = StrollCountEntity.builder()
	                .user(userEntity)  // UserEntity連結
	                .strollDate(LocalDateTime.now())  // 現在の時間を使う
	                .isStrolled("Y")  // 散歩の存否'y'
	                .build();
	        
	        // StrollCountEntityを追加(アップデート)
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
	
	// ポストデリート
	@Override
	@Transactional
	public void deleteFeed(String feedid) {
		// ポストの情報を抽出して取得
        FeedEntity feedEntity = feedRepository.findById(feedid)
                .orElseThrow(() -> new RuntimeException("ポストが見つかりません。"));

        // 該当するポストに関われたリプライを削除
        commentRepository.deleteByFeed(feedEntity);

        // 該当するポストに関われたいいねを削除
        feedLikeRepository.deleteByFeed(feedEntity);

        // ポストを削除
        feedRepository.delete(feedEntity);

        // ポスト削除後、StrollCountEntityに影響が与えられないようにしてis_strolledはそのままyにする
        // StrollCountEntityでFeedEntityと連結されている内容は削除しない
	}
	
	// ユーザーの情報を抽出して取得
	@Override
	public List<FeedDTO> myFeedSelect(String userid) {
		List<Object[]> results = feedRepository.myFeedSelect(userid);
	    List<FeedDTO> feedDTOs = new ArrayList<>();

	    for (Object[] row : results) {
	        FeedDTO feedDTO = new FeedDTO(
	            (String) row[0],  							// feedid
	            convertToLocalDateTime((Timestamp) row[1]), // feedcreatedate
	            (Integer) row[2], 							// feedlike
	            (Integer) row[3],  							// feedstatus
	            (String) row[4],  							// picture
	            (String) row[5]    							// userid
	        );
	        feedDTOs.add(feedDTO);
	    }

	    return feedDTOs;
	}
	
	// 全ポストの情報を抽出して取得ポスト
	@Override
	public List<SelectAllDTO> feedSelectAll() {
		
		List<Object[]> results = feedRepository.findAllFeedsWithLikes();
		
		return results.stream()
                 .map(result -> new SelectAllDTO(
                     (String) result[0],  	// userId
                     (String) result[1],  	// feedId
                     (Timestamp) result[2],	// feedCreateDate
                     (Integer) result[3],  	// feedLike
                     (Integer) result[4],  	// feedStatus
                     (String) result[5],  	// likeUserId
                     (String) result[6]   	// likeFeedId
                 ))
                 .collect(Collectors.toList());
	}
	
	// ポストの詳細情報を抽出して取得ポスト(ポストとリプライ)
	@Override
	public FeedWithCommentsDTO getFeedWithComments(String userid, String feedid) {
		// ユーザーとポストの情報を抽出して取得ポスト
	    UserEntity user = userRepository.findById(userid)
	            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません。"));

	    FeedEntity feed = feedRepository.findById(feedid)
	            .orElseThrow(() -> new RuntimeException("ポストが見つかりません。"));

	    // ポスト情報をDTOに変換
	    FeedDTO feedDTO = new FeedDTO(
	            feed.getFeedid(),
	            feed.getFeedcreatedate(),
	            feed.getFeedlike(),
	            feed.getFeedstatus(),
	            feed.getPicture(),
	            feed.getKawaiinuuserfeedid().getUserid()
	    );

	    // 該当するポストに作成されているリプライのリスト情報を抽出して取得ポストポスト
	    List<CommentsEntity> comments = commentRepository.findByFeedAndUser(feed, user);

	    // 作成されているリプライの情報をDTOに変換
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

	    // ポストとリプライの情報をFeedWithCommentsDTOにして返還
	    return new FeedWithCommentsDTO(feedDTO, commentsDTOList);
	}
	
	// リプライ追加
	@Override
	public List<CommentsDTO> addCommentToFeed(CommentsDTO commentsDTO) {
		// ユーザーとポストの情報を抽出して取得ポストポスト
        UserEntity user = userRepository.findById(commentsDTO.getUserid())
				.orElseThrow(() -> new RuntimeException("ユーザーが見つかりません。"));
		
		FeedEntity feed = feedRepository.findById(commentsDTO.getFeedid())
			        .orElseThrow(() -> new RuntimeException("ポストが見つかりません。"));
		
		CommentsEntity comment = CommentsEntity.builder()
		    .user(user)
		    .feed(feed)
		    .comments(commentsDTO.getComments())
		    .commentscreatedate(LocalDateTime.now())
		    .build();
		
		// リプライを追加
		commentRepository.save(comment);
		
		// ポストにリプライの情報を追加
		feed.addComment(comment);
		// ポストの情報をアップデートしてリプライのリスト情報をアップデート
		feedRepository.save(feed);  
		
		// ポストを作成したユーザーが本人のポストに作成したリプライはアラームテーブルに追加しない
		if(!user.getUserid().equals(feed.getKawaiinuuserfeedid().getUserid())) {
			alarmService.createLikeOrCommentAlarm(user.getUserid(), feed, 'C');			
		}
		
		// ポストに作成されている全リプライの情報を抽出して取得ポストポスト
	    List<CommentsEntity> allComments = commentRepository.findByFeed(feed);

	    // 全リプライDTOに変換してリストにして返還
	    List<CommentsDTO> commentsDTOList = allComments.stream()
	            .map(c -> new CommentsDTO(
	            		c.getCommentsid(),
	                    c.getFeed().getFeedid(),
	                    c.getUser().getUsernickname(),
	                    c.getUser().getUserid(),
	                    c.getComments(),
	                    c.getCommentscreatedate()
	            ))
	            .collect(Collectors.toList());
	    return commentsDTOList;
	}
	
	// リプライ修正(アップデート)
	@Override
	public CommentsDTO updateComment(CommentsDTO commentsDTO) {
	    CommentsEntity comment = commentRepository.findById(commentsDTO.getCommentsid())
	            .orElseThrow(() -> new RuntimeException("リプライが見つかりません。"));

	    // リプライの内容アップデート
	    comment.setComments(commentsDTO.getComments());
	    comment.setCommentscreatedate(LocalDateTime.now()); // 修正時間アップデート

	    commentRepository.save(comment);

	    // アップデートされたリプライをDTOにして返還
	    return new CommentsDTO(
	            comment.getCommentsid(),
	            comment.getFeed().getFeedid(),
	            comment.getUser().getUsernickname(),
	            comment.getUser().getUserid(),
	            comment.getComments(),
	            comment.getCommentscreatedate()
	    );
	}
	
	// リプライ情報を抽出して取得
	@Override
	public List<CommentsDTO> getComment(String feedId) {
		FeedEntity feed = feedRepository.findById(feedId)
		        .orElseThrow(() -> new RuntimeException("ポストが見つかりません。"));
		
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

	// リプライ削除
	@Override
	@Transactional
	public void deleteComment(CommentsDTO commentsDTO) {		
        CommentsEntity commentsEntity = commentRepository.findById(commentsDTO.getCommentsid())
        		.orElseThrow(() -> new RuntimeException("リプライが見つかりません。"));

        commentRepository.delete(commentsEntity);
	}
	
	// いいね＆いいねキャンセル
	@Override
	@Transactional
	public void toggleLike(String userid, String feedid) {
	    // ユーザーとポストの情報を抽出して取得
	    UserEntity user = userRepository.findById(userid)
	            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません。"));
	    FeedEntity feed = feedRepository.findById(feedid)
	            .orElseThrow(() -> new RuntimeException("ポストが見つかりません。"));

	    // ユーザーが該当するポストにいいねを押下したかを確認
	    boolean alreadyLiked = feedLikeRepository.existsByUserAndFeed(user, feed);

	    if (alreadyLiked) {
	        // いいねキャンセル
	        feedLikeRepository.deleteByUserAndFeed(user, feed);
	        feed.setFeedlike(feed.getFeedlike() - 1);  // ポストのいいね数が下がる
	    } else {
	        // いいね追加
	        FeedLikeEntity feedLike = new FeedLikeEntity();
	        feedLike.setUser(user);
	        feedLike.setFeed(feed);
	        feedLike.setLikedate(LocalDateTime.now());
	        feedLikeRepository.save(feedLike);
	        feed.setFeedlike(feed.getFeedlike() + 1);  // ポストのいいねの数が上がる
	    }

	    // ポストアップデート
	    feedRepository.save(feed);
	    
	    // ポストを作成したユーザーが本人のポストに作成したリプライはアラームテーブルに追加しない
 		if(!user.getUserid().equals(feed.getKawaiinuuserfeedid().getUserid())) {
 			alarmService.createLikeOrCommentAlarm(user.getUserid(), feed, 'L');			
 		}
	}
	
	// いいねの数の情報を抽出して取得
	@Override
	public long getFeedLikeCount(String feedid) {
	    FeedEntity feed = feedRepository.findById(feedid)
	            .orElseThrow(() -> new RuntimeException("ポストが見つかりません。"));
	    return feedLikeRepository.countByFeed(feed);  // ポストに作成されているいいねの総個数を返還
	}
	
	
	// ポストIDでポストの情報を抽出して取得
	@Override
    public FeedEntity findFeedById(String feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("ポストが見つかりません。"));
    }

    // ポストの状態をアップデート
	@Override
    public void saveFeed(FeedEntity feedEntity) {
        feedRepository.save(feedEntity);
    }
	
	// TimestampをLocalDateTimeに変換するユーティリティメソッド
	private LocalDateTime convertToLocalDateTime(Timestamp timestamp) {
	    return timestamp.toLocalDateTime();  // TimestampをLocalDateTimeに変換
	}
}
