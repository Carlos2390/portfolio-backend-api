package com.cmatos.portfolio_backend_api.services;

import com.cmatos.portfolio_backend_api.exception.ProjectNotFoundException;
import com.cmatos.portfolio_backend_api.model.Comment;
import com.cmatos.portfolio_backend_api.model.Project;
import com.cmatos.portfolio_backend_api.model.Skill;
import com.cmatos.portfolio_backend_api.model.User;
import com.cmatos.portfolio_backend_api.records.*;
import com.cmatos.portfolio_backend_api.repository.ProjectRepository;
import com.cmatos.portfolio_backend_api.repository.specs.ProjectSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SkillService skillService;

    @Autowired
    private CommentService commentService;

    public ProjectResponseDTO findProjectById(Long id) {
        Project project = projectRepository.findProjectById(id);
        if (project == null) {
            throw new ProjectNotFoundException("Projeto não encontrado");
        }
        return entityToResponse(project);
    }

    public Page<ProjectResponseDTO> getAllProjectsPageable(ProjectFilterDTO filters, Pageable pageable) {
        Specification<Project> spec = (root, query, cb) -> null;
        if (filters != null) {
            if (filters.name() != null && !filters.name().isBlank()) {
                spec = spec.and(ProjectSpecifications.nameContain(filters.name()));
            }
            if (filters.skills() != null && !filters.skills().isEmpty()) {
                spec = spec.and(ProjectSpecifications.containSkills(filters.skills()));
            }
        }
        return projectRepository.findAll(spec, pageable).map(this::entityToResponse);
    }

    public Page<ProjectResponseDTO> getProjectsBySessionUser(ProjectFilterDTO filters, Pageable pageable) {
        User sessionUser = getSessionUser();
        Specification<Project> spec = Specification.where(ProjectSpecifications.userIdIs(sessionUser.getId()));
        if (filters != null) {
            if (filters.name() != null && !filters.name().isBlank()) {
                spec = spec.and(ProjectSpecifications.nameContain(filters.name()));
            }
            if (filters.skills() != null && !filters.skills().isEmpty()) {
                spec = spec.and(ProjectSpecifications.containSkills(filters.skills()));
            }
        }
        return projectRepository.findAll(spec, pageable).map(this::entityToResponse);
    }

    public ProjectResponseDTO save(ProjectDTO dto) {
        Project project = createEditProject(new Project(), dto);
        User sessionUser = getSessionUser();
        project.setUserId(sessionUser.getId());
        associateSkillsToProject(project, dto.skills());
        projectRepository.save(project);
        project.setUser(sessionUser);
        return entityToResponse(project);
    }

    public void edit(Long projectId, ProjectDTO editedProject) {
        Project projectById = projectRepository.findProjectById(projectId);
        if (projectById == null) {
            throw new ProjectNotFoundException("Projeto não encontrado");
        }
        if (!projectById.getUserId().equals(getSessionUser().getId())) {
            throw new AccessDeniedException("Projeto não pertence ao usuário que está tentando realizar a edição");
        }
        associateSkillsToProject(projectById, editedProject.skills());
        projectRepository.save(createEditProject(projectById, editedProject));
    }

    private void associateSkillsToProject(Project project, List<SkillDTO> skills) {
        if (skills == null || skills.isEmpty()) {
            project.getSkills().clear();
            return;
        }
        List<String> skillNames = skills.stream().map(s -> s.name().toLowerCase().trim()).distinct().toList();
        List<Skill> existingSkills = skillService.findAllByNames(skillNames);

        List<String> existingNames = existingSkills.stream().map(Skill::getName).toList();

        List<Skill> newSkills = skillNames.stream().filter(name -> !existingNames.contains(name))
                .map(name -> {
                    Skill skill = new Skill();
                    skill.setName(name);
                    return skill;
                }).toList();

        if (!newSkills.isEmpty()) {
            skillService.saveAll(newSkills);
        }

        HashSet<Skill> allSkills = new HashSet<>(existingSkills);
        allSkills.addAll(newSkills);
        project.setSkills(allSkills);
    }

    public Project createEditProject(Project entity, ProjectDTO dto) {
        entity.setName(dto.name());
        entity.setDocumentationUrl(dto.documentationUrl());
        entity.setInterfaceUrl(dto.interfaceUrl());
        entity.setGithubUrl(dto.githubUrl());
        entity.setImageUrl(dto.imageUrl());
        entity.setObservations(dto.observations());
        return entity;
    }

    public void deleteProject(Long projectId) {
        Project projectById = projectRepository.findProjectById(projectId);
        if (projectById == null) {
            throw new ProjectNotFoundException("Projeto não encontrado");
        }
        if (!projectById.getUserId().equals(getSessionUser().getId())) {
            throw new AccessDeniedException("Projeto não pertence ao usuário que está tentando realizar a exclusão");
        }
        projectRepository.delete(projectById);
    }

    public void addComment(Long projectId, String comment) {
        Project project = projectRepository.findProjectById(projectId);
        if (project == null) {
            throw new ProjectNotFoundException("Projeto não encontrado");
        }
        User sessionUser = getSessionUser();
        Comment commentEntity = new Comment();
        commentEntity.setProjectId(projectId);
        commentEntity.setComment(comment);
        commentEntity.setUserId(sessionUser.getId());
        project.getComments().add(commentEntity);
        projectRepository.save(project);
    }

    public Page<CommentDTO> listComments(Long projectId, Pageable pageable) {
        Project project = projectRepository.findProjectById(projectId);
        if (project == null) {
            throw new ProjectNotFoundException("Projeto não encontrado");
        }
        return commentService.findCommentsByProjectId(projectId, pageable);
    }

    private User getSessionUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("Usuário não autenticado");
        }
        return (User) authentication.getPrincipal();
    }

    private ProjectResponseDTO entityToResponse(Project entity) {
        return new ProjectResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getUser().getUsernameExibition(),
                entity.getDocumentationUrl(),
                entity.getInterfaceUrl(),
                entity.getGithubUrl(),
                entity.getImageUrl(),
                entity.getObservations(),
                entity.getUpdatedAt(),
                entity.getSkills().stream().map(skillService::entityToDTO).toList()
        );
    }
}
