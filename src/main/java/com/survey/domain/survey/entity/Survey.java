package com.survey.domain.survey.entity;

import com.survey.domain.audit.BaseEntity;
import com.survey.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description; //설문지 설명

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @Column(name = "start_at")
    private LocalDateTime startAt; //설문조사 시작 시간

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @Column(name = "end_at")
    private LocalDateTime endAt; //설문조사 마감 시간

    @Min(0)
    @Column(name = "user_limit")
    private Integer userLimit; //선착순 인원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "category_id")
    private Long categoryId; //survey와 생명주기가 일치하지 않는거 같아서 의존관계로 설정

    @Builder
    public Survey(Long id, String title, String description, LocalDateTime startAt, LocalDateTime endAt, Integer userLimit, User user, Long categoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.userLimit = userLimit;
        this.user = user;
        this.categoryId = categoryId;
    }

    //startAt, endAt 날짜가 현재 날짜나 startAt보다 앞서가면 오류
    public void validateDateTime(LocalDateTime startAt, LocalDateTime endAt) {

        if(startAt != null) {
            if(startAt.isBefore(LocalDateTime.now())) throw new IllegalStateException("오늘 이전 날짜로는 생성 불가");
        }
        if(endAt != null) {
            if(startAt != null) {
                if(endAt.isBefore(startAt)) throw new IllegalStateException("시작 날짜 이전으로 설정 불가");
            } else {
                if(endAt.isBefore(LocalDateTime.now())) throw new IllegalStateException("오늘 이전 날짜로는 생성 불가");
            }
        }
    }
    //survey 수정
    public void updateSurvey(String title, String description, String startAt, String endAt, Integer userLimit, Long categoryId) {
        LocalDateTime startAt1 = startAt != null ? LocalDateTime.parse(startAt) : null;
        LocalDateTime endAt1 = endAt != null ? LocalDateTime.parse(endAt) : null;

        validateDateTime(startAt1, endAt1);

        if(title != null) {
            this.title = title;
        }
        if(description != null) {
            this.description = description;
        }
        if(startAt1 != null) {
            this.startAt = startAt1;
        }
        if(endAt1 != null) {
            this.endAt = endAt1;
        }
        if(userLimit != null) {
            this.userLimit = userLimit;
        }
        if(categoryId != null) {
            this.categoryId = categoryId;
        }
    }


}
