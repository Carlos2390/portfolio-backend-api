package com.cmatos.portfolio_backend_api.controller;

import com.cmatos.portfolio_backend_api.records.CommentDTO;
import com.cmatos.portfolio_backend_api.records.ProjectDTO;
import com.cmatos.portfolio_backend_api.records.ProjectResponseDTO;
import com.cmatos.portfolio_backend_api.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
@Tag(name = "Projetos", description = "Endpoints para gerenciamento de projetos")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/all")
    @Operation(summary = "Busca todos os projetos")
    @ApiResponse(responseCode = "200", description = "Projetos encontrados com sucesso")
    public ResponseEntity<Page<ProjectResponseDTO>> getAllProjectsPageable(
            @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(projectService.getAllProjectsPageable(pageable));
    }

    @GetMapping
    @Operation(summary = "Busca todos os projetos do usuário logado")
    @ApiResponse(responseCode = "200", description = "Projetos encontrados com sucesso")
    public ResponseEntity<Page<ProjectResponseDTO>> getProjectsBySessionUser(
            @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(projectService.getProjectsBySessionUser(pageable));
    }

    @Operation(summary = "Cria um novo projeto e retorna o projeto criado com o ID")
    @ApiResponse(responseCode = "200", description = "Projeto criado com sucesso")
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody @Valid ProjectDTO dto) {
        ProjectResponseDTO savedProject = projectService.save(dto);
        return ResponseEntity.ok(savedProject);
    }

    @Operation(summary = "Edita um projeto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto editado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Projeto não pertence ao usuário logado"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> editProject(@PathVariable Long id, @RequestBody @Valid ProjectDTO dto) {
        try {
            projectService.edit(id, dto);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Deleta um projeto que pertence ao usuário logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto deletado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Projeto não pertence ao usuário logado"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Adiciona um comentário a um projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<Void> addComment(@PathVariable Long id, @RequestBody String comment) {
        try {
            projectService.addComment(id, comment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Lista os comentários de um projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentários encontrados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentDTO>> listProjectComments(@PathVariable Long id, @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            Page<CommentDTO> comments = projectService.listComments(id, pageable);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
