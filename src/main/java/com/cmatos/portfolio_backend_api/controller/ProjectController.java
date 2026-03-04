package com.cmatos.portfolio_backend_api.controller;

import com.cmatos.portfolio_backend_api.records.*;
import com.cmatos.portfolio_backend_api.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
@Tag(name = "Projetos", description = "Endpoints para gerenciamento de projetos")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar o projeto pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ProjectResponseDTO findProjectById(@PathVariable Long id) {
        return projectService.findProjectById(id);
    }

    @PostMapping("/all")
    @Operation(summary = "Busca todos os projetos")
    @ApiResponse(responseCode = "200", description = "Projetos encontrados com sucesso")
    public ResponseEntity<Page<ProjectResponseDTO>> getAllProjectsPageable(
            @RequestBody(required = false) ProjectFilterDTO filters,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(projectService.getAllProjectsPageable(filters, pageable));
    }

    @PostMapping("/bySessionUser")
    @Operation(summary = "Busca todos os projetos do usuário logado")
    @ApiResponse(responseCode = "200", description = "Projetos encontrados com sucesso")
    public ResponseEntity<Page<ProjectResponseDTO>> getProjectsBySessionUser(
            @RequestBody(required = false) ProjectFilterDTO filters,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(projectService.getProjectsBySessionUser(filters, pageable));
    }

    @Operation(summary = "Cria um novo projeto e retorna o projeto criado com o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class),
                            mediaType = "application/json")),
    })
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody @Valid ProjectDTO dto) {
        ProjectResponseDTO savedProject = projectService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }

    @Operation(summary = "Edita um projeto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto editado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class),
                            mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Projeto não pertence ao usuário logado"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> editProject(@PathVariable Long id, @RequestBody @Valid ProjectDTO dto) {
        projectService.edit(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Deleta um projeto que pertence ao usuário logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto deletado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Projeto não pertence ao usuário logado"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Adiciona um comentário a um projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<Void> addComment(@PathVariable Long id, @RequestBody String comment) {
        projectService.addComment(id, comment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Lista os comentários de um projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentários encontrados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentDTO>> listProjectComments(@PathVariable Long id, @ParameterObject Pageable pageable) {
        Page<CommentDTO> comments = projectService.listComments(id, pageable);
        return ResponseEntity.ok(comments);
    }

}
