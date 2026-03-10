package com.bank.api.controller;

import com.bank.api.dto.TransferenciaDTO;
import com.bank.api.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transferencias")
@CrossOrigin(origins = "*")
public class TransferenciaController {

    @Autowired
    private TransferenciaService transferenciaService;

    @PostMapping
    public ResponseEntity<TransferenciaDTO> realizarTransferencia(
            @RequestParam Long usuarioOrigemId,
            @RequestParam Long usuarioDestinoId,
            @RequestParam BigDecimal valor,
            @RequestParam(required = false) String descricao,
            @RequestParam(defaultValue = "ENTRADA") String tipoTransferencia) {
        TransferenciaDTO transferenciaDTO = transferenciaService.realizarTransferencia(
                usuarioOrigemId, usuarioDestinoId, valor, descricao, tipoTransferencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(transferenciaDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaDTO> obterPorId(@PathVariable Long id) {
        TransferenciaDTO transferenciaDTO = transferenciaService.obterPorId(id);
        return ResponseEntity.ok(transferenciaDTO);
    }

    @GetMapping("/enviadas/{usuarioId}")
    public ResponseEntity<List<TransferenciaDTO>> listarEnviadas(@PathVariable Long usuarioId) {
        List<TransferenciaDTO> transferencias = transferenciaService.listarEnviadas(usuarioId);
        return ResponseEntity.ok(transferencias);
    }

    @GetMapping("/recebidas/{usuarioId}")
    public ResponseEntity<List<TransferenciaDTO>> listarRecebidas(@PathVariable Long usuarioId) {
        List<TransferenciaDTO> transferencias = transferenciaService.listarRecebidas(usuarioId);
        return ResponseEntity.ok(transferencias);
    }

    @GetMapping("/todas/{usuarioId}")
    public ResponseEntity<List<TransferenciaDTO>> listarTodas(@PathVariable Long usuarioId) {
        List<TransferenciaDTO> transferencias = transferenciaService.listarTodas(usuarioId);
        return ResponseEntity.ok(transferencias);
    }
}

