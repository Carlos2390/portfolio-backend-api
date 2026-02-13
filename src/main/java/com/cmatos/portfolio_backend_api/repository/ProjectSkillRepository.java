package com.cmatos.portfolio_backend_api.repository;

import com.cmatos.portfolio_backend_api.model.ProjectSkills;
import com.cmatos.portfolio_backend_api.model.ProjectSkillsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectSkillRepository extends JpaRepository<ProjectSkills, ProjectSkillsId> {
}
