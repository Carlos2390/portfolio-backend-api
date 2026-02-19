package com.cmatos.portfolio_backend_api.services;

import com.cmatos.portfolio_backend_api.model.Project;
import com.cmatos.portfolio_backend_api.model.User;
import com.cmatos.portfolio_backend_api.records.ProjectDTO;
import com.cmatos.portfolio_backend_api.records.SkillDTO;
import com.cmatos.portfolio_backend_api.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private SkillService skillService;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldCreateProject() {
        User sessioUser = new User();
        sessioUser.setId(1L);
        mockSecurityContext(sessioUser);

        ProjectDTO project = createProjectDTO();
        projectService.save(project);
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void shouldEditProject() {
        User sessionUser = new User();
        sessionUser.setId(1L);
        mockSecurityContext(sessionUser);

        Project projectDB = createProject(1L);
        when(projectRepository.findProjectById(1L)).thenReturn(projectDB);

        projectService.edit(1L, createProjectDTO());

        verify(projectRepository).save(argThat(savedProject ->
                savedProject.getName().equals("TEST") &&
                        savedProject.getUserId().equals(1L)
        ));
        verify(projectRepository).findProjectById(1L);
    }

    @Test
    void shouldOnlyEditIfItBelongsToTheUser() {
        User sessioUser = new User();
        sessioUser.setId(1L);
        mockSecurityContext(sessioUser);

        Project project = createProject(2L);
        when(projectRepository.findProjectById(1L)).thenReturn(project);

        assertThrows(AccessDeniedException.class,
                () -> projectService.edit(1L, createProjectDTO()));
        verify(projectRepository).findProjectById(1L);
    }

    private ProjectDTO createProjectDTO() {
        return new ProjectDTO(
                "TEST",
                "TEST",
                "TEST",
                "TEST",
                "TEST",
                "TEST",
                List.of(new SkillDTO("TEST"))
        );
    }

    private Project createProject(Long userId) {
        Project project = new Project();
        project.setId(1L);
        project.setName("TEST");
        project.setUserId(userId);
        return project;
    }

    private void mockSecurityContext(User user) {
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }
}
