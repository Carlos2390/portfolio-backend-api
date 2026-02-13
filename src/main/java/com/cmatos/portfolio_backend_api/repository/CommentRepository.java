package com.cmatos.portfolio_backend_api.repository;

import com.cmatos.portfolio_backend_api.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findCommentByProjectIdOrderByCreatedAtDesc(Long projectId);

    List<Comment> findCommentByUserIdOrderByCreatedAtDesc(Long userId);
}
