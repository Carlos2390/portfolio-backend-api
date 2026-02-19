package com.cmatos.portfolio_backend_api.repository;

import com.cmatos.portfolio_backend_api.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findSkillsByNameIn(List<String> names);
}
