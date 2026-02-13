package com.cmatos.portfolio_backend_api.repository;

import com.cmatos.portfolio_backend_api.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
}
