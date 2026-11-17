package com.softwareprojekt.gruppe8.bewertung.repository;

import com.softwareprojekt.gruppe8.bewertung.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}