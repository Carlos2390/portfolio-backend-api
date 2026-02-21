package com.cmatos.portfolio_backend_api.repository;

import com.cmatos.portfolio_backend_api.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    Page<Comment> findCommentsByProject_IdOrderByCreatedAt(Long projectId, Pageable pageable);
}
