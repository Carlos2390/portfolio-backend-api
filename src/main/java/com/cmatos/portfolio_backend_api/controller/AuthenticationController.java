package com.cmatos.portfolio_backend_api.controller;

import com.cmatos.portfolio_backend_api.configuration.security.TokenService;
import com.cmatos.portfolio_backend_api.model.User;
import com.cmatos.portfolio_backend_api.records.AuthenticationDTO;
import com.cmatos.portfolio_backend_api.records.LoginResponseDTO;
import com.cmatos.portfolio_backend_api.records.UserDTO;
import com.cmatos.portfolio_backend_api.records.UserResponseDTO;
import com.cmatos.portfolio_backend_api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints para registro e autenticação do usuário")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Endpoint para autenticação do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Usuário ou senha inválidos")
    })
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO dto) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Endpoint para registro do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Usuário já existe")
    })
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserDTO dto) {
        if (userService.verifyExistingUser(dto)) {
            return ResponseEntity.badRequest().body("Usuário já existe");
        }
        userService.save(dto);

        return ResponseEntity.ok().body("Usuário registrado com sucesso");
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getUserSessionAttributes() {
        return ResponseEntity.ok(userService.getSessioUserResponseDTO());
    }
}
