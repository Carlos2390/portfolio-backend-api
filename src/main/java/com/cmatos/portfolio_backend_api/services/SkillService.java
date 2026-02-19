package com.cmatos.portfolio_backend_api.services;

import com.cmatos.portfolio_backend_api.model.Skill;
import com.cmatos.portfolio_backend_api.records.SkillDTO;
import com.cmatos.portfolio_backend_api.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public List<Skill> findAllByNames(List<String> names) {
        return skillRepository.findSkillsByNameIn(names);
    }

    public void saveAll(List<Skill> skills) {
        skillRepository.saveAll(skills);
    }

    public SkillDTO entityToDTO(Skill entity) {
        return new SkillDTO(entity.getName());
    }
}
