package com.project.kawaiinu.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "stroll_count")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class StrollCountEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stroll_id", nullable = false)
    private Long strollId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")  // 외래키로 UserEntity와 연관
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedid")  // FeedEntity와 연관
    private FeedEntity feed;  // 해당 StrollCountEntity가 연관된 FeedEntity

    @Column(name = "stroll_date", nullable = false)
    private LocalDateTime strollDate;  // 산책 날짜

    @Column(name = "is_strolled", nullable = false)
    private String isStrolled;  // 산책 여부 ('y' or 'n')
}
