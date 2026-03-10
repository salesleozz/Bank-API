package com.bank.api.controller;

import com.bank.api.dto.CartaoDTO;
import com.bank.api.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cartoes")
@CrossOrigin(origins = "*")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<CartaoDTO> criar(
            @RequestParam Long usuarioId,
            @RequestParam String titular,
            @RequestParam String dataValidade,
            @RequestParam BigDecimal limiteCredito,
            @RequestParam String tipoCartao) {
        CartaoDTO cartaoDTO = cartaoService.criar(usuarioId, titular, dataValidade, limiteCredito, tipoCartao);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartaoDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartaoDTO> obterPorId(@PathVariable Long id) {
        CartaoDTO cartaoDTO = cartaoService.obterPorId(id);
        return ResponseEntity.ok(cartaoDTO);
    }

    @GetMapping("/numero/{numeroCartao}")
    public ResponseEntity<CartaoDTO> obterPorNumeroCartao(@PathVariable String numeroCartao) {
        CartaoDTO cartaoDTO = cartaoService.obterPorNumeroCartao(numeroCartao);
        return ResponseEntity.ok(cartaoDTO);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CartaoDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<CartaoDTO> cartoes = cartaoService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(cartoes);
    }

    @PostMapping("/{id}/bloquear")
    public ResponseEntity<CartaoDTO> bloquearCartao(@PathVariable Long id) {
        CartaoDTO cartaoDTO = cartaoService.bloquearCartao(id);
        return ResponseEntity.ok(cartaoDTO);
    }

    @PostMapping("/{id}/desbloquear")
    public ResponseEntity<CartaoDTO> desbloquearCartao(@PathVariable Long id) {
        CartaoDTO cartaoDTO = cartaoService.desbloquearCartao(id);
        return ResponseEntity.ok(cartaoDTO);
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<CartaoDTO> cancelarCartao(@PathVariable Long id) {
        CartaoDTO cartaoDTO = cartaoService.cancelarCartao(id);
        return ResponseEntity.ok(cartaoDTO);
    }

    @PostMapping("/{id}/compra")
    public ResponseEntity<CartaoDTO> realizarCompra(
            @PathVariable Long id,
            @RequestParam BigDecimal valor,
            @RequestParam(required = false) String descricao) {
        CartaoDTO cartaoDTO = cartaoService.realizarCompra(id, valor, descricao);
        return ResponseEntity.ok(cartaoDTO);
    }

    @PostMapping("/{id}/pagar-fatura")
    public ResponseEntity<CartaoDTO> pagarFatura(
            @PathVariable Long id,
            @RequestParam BigDecimal valor) {
        CartaoDTO cartaoDTO = cartaoService.pagarFatura(id, valor);
        return ResponseEntity.ok(cartaoDTO);
    }
}

