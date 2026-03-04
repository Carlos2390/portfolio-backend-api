package com.cmatos.portfolio_backend_api.controller;

import com.cmatos.portfolio_backend_api.records.SkillDTO;
import com.cmatos.portfolio_backend_api.services.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/skills")
@Tag(name = "Skills", description = "Endpoint para listar todas as skills já cadastradas")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @Operation(summary = "Busca todas as skills")
    @ApiResponse(responseCode = "200", description = "Skills encontradas com sucesso")
    @GetMapping
    public ResponseEntity<List<SkillDTO>> getAllSkills() {
        return ResponseEntity.ok(skillService.findAllSkills());
    }
}
