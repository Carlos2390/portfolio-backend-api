package com.cmatos.portfolio_backend_api.controller;

import com.cmatos.portfolio_backend_api.records.HealthResponse;
import com.cmatos.portfolio_backend_api.services.HealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
@Tag(name = "Status", description = "Endpoint para verificação de status do serviço")
public class StatusController {

    @Autowired
    private HealthService healthService;

    @Operation(summary = "Busca a versão, o maximo de conexões e as conexões abertas do banco de dados.")
    @ApiResponse(responseCode = "200", description = "Conexão estabelecida com sucesso.")
    @GetMapping
    public ResponseEntity<HealthResponse> getStatus() {
        return ResponseEntity.ok(healthService.getHealth());
    }
}
