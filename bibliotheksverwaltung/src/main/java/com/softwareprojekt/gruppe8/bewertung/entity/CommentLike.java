package com.softwareprojekt.gruppe8.bewertung.entity;

import com.softwareprojekt.gruppe8.data.AbstractEntity;
import com.softwareprojekt.gruppe8.konto.entity.User;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "comment_like",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_comment_like_user_comment",
                columnNames = {"user_id", "comment_id"}
        )
)
public class CommentLike extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public CommentLike() {
    }

    @PrePersist
    private void onCreate() {
        createdAt = Instant.now();
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}