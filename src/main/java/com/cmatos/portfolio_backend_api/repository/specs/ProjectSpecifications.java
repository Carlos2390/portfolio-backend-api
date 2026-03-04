package com.cmatos.portfolio_backend_api.repository.specs;

import com.cmatos.portfolio_backend_api.model.Project;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProjectSpecifications {

    public static Specification<Project> userIdIs(Long userId) {
        if (userId == null || userId == 0L) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userId"), userId);
    }

    public static Specification<Project> nameContain(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Project> containSkills(List<String> skills) {
        return (root, query, criteriaBuilder) -> {
            if (skills == null || skills.isEmpty()) {
                return null;
            }
            query.distinct(true);
            return root.join("skills").get("name").in(skills.stream().map(String::toLowerCase).toList());
        };
    }
}
