package com.cmatos.portfolio_backend_api.repository;

import com.cmatos.portfolio_backend_api.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    Page<Project> findProjectByUserIdOrderByCreatedAt(Long userId, Pageable pageable);

    Project findProjectById(Long id);

    @Override
    Page<Project> findAll(Specification<Project> spec, Pageable pageable);
}
