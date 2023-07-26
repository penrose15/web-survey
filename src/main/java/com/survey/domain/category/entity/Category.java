package com.survey.domain.category.entity;

import com.survey.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Category(Long id, String category, User user) {
        this.id = id;
        this.category = category;
        this.user = user;
    }

    public void createCategory(String category, User user) {
        this.category = category;
        this.user = user;
    }

    public void updateCategory(String category) {
        if(category != null) {
            this.category = category;
        }
    }
}
