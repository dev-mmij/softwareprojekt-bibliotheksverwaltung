package com.softwareprojekt.gruppe8.bewertung.repository;

import com.softwareprojekt.gruppe8.bewertung.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}