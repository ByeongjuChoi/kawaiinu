package com.project.kawaiinu.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "kawaiinu_user_feed")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class FeedEntity {

	@Id
	@Column(name = "feedid", nullable = false, unique = true, length = 50)
	@NotBlank(message = "Feed ID는 필수 입력값입니다.")
	private String feedid;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid") // 外来キーを表すコラム名
    private UserEntity kawaiinuuserfeedid;
    
	@Column(name = "picture")
	private String picture;		
	
	@Column(name = "feedlike")
	private int feedlike;		// いいねの数
	
	@Column(name = "feedcreatedate")
	private LocalDateTime feedcreatedate;	// ポストの生成日、タイプをdateに変更しなければならない
	
	@Column(name = "feedstatus")
	private int feedstatus;		// ポストの状態0非公開、1公開
	
	@OneToMany(mappedBy = "feed", fetch = FetchType.EAGER)
	private List<CommentsEntity> comments = new ArrayList<>();		// リプライ
	
    @OneToMany(mappedBy = "feed", fetch = FetchType.EAGER)
    private List<StrollCountEntity> strollCounts = new ArrayList<>();

	public void setUser(final UserEntity user) {
		this.kawaiinuuserfeedid = user;
	}
	
	// リプライを追加するメソッド(便利に追加出来る)
	public void addComment(CommentsEntity comment) {
		this.comments.add(comment);
	}
	
	// ポストの状態を変更するメソッド
    public void setFeedStatus(int feedstatus) {
        this.feedstatus = feedstatus;
    }
}
