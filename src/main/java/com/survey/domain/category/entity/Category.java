package com.survey.domain.category.entity;

import com.survey.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false, length = 100)
    private String category;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Builder
    public Category(Long id, String category, Long userId) {
        this.id = id;
        this.category = category;
        this.userId = userId;
    }

    public void updateCategory(String category) {
        if(category != null) {
            this.category = category;
        }
    }

}
