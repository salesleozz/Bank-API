package com.bank.api.controller;

import com.bank.api.dto.UsuarioCriacaoDTO;
import com.bank.api.dto.UsuarioDTO;
import com.bank.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody UsuarioCriacaoDTO dto) {
        UsuarioDTO usuarioDTO = usuarioService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obterPorId(@PathVariable Long id) {
        UsuarioDTO usuarioDTO = usuarioService.obterPorId(id);
        return ResponseEntity.ok(usuarioDTO);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> obterPorEmail(@PathVariable String email) {
        UsuarioDTO usuarioDTO = usuarioService.obterPorEmail(email);
        return ResponseEntity.ok(usuarioDTO);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obterTodos() {
        List<UsuarioDTO> usuarios = usuarioService.obterTodos();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioCriacaoDTO dto) {
        UsuarioDTO usuarioDTO = usuarioService.atualizar(id, dto);
        return ResponseEntity.ok(usuarioDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

