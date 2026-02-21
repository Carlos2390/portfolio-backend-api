package com.cmatos.portfolio_backend_api.services;

import com.cmatos.portfolio_backend_api.model.Comment;
import com.cmatos.portfolio_backend_api.records.CommentDTO;
import com.cmatos.portfolio_backend_api.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Page<CommentDTO> findCommentsByProjectId(Long projectId, Pageable pageable) {
        return commentRepository.findCommentsByProject_IdOrderByCreatedAt(projectId, pageable).map(this::entityToDTO);
    }

    public CommentDTO entityToDTO(Comment entity) {
        return new CommentDTO(
                entity.getComment(),
                entity.getProjectId(),
                entity.getUser().getUsernameExibition()
        );
    }
}
