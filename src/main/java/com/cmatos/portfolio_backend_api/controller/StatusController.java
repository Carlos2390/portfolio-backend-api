package com.cmatos.portfolio_backend_api.controller;

import com.cmatos.portfolio_backend_api.records.HealthResponse;
import com.cmatos.portfolio_backend_api.services.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private HealthService healthService;

    @GetMapping
    public ResponseEntity<HealthResponse> getStatus() {
        return ResponseEntity.ok(healthService.getHealth());
    }
}
