package com.cmatos.portfolio_backend_api.repository;

import com.cmatos.portfolio_backend_api.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findProjectByUserIdOrderByCreatedAt(Long userId);
}
