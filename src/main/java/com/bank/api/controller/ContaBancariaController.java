package com.bank.api.controller;

import com.bank.api.dto.ContaBancariaDTO;
import com.bank.api.service.ContaBancariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/contas")
@CrossOrigin(origins = "*")
public class ContaBancariaController {

    @Autowired
    private ContaBancariaService contaBancariaService;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ContaBancariaDTO> obterPorUsuarioId(@PathVariable Long usuarioId) {
        ContaBancariaDTO contaDTO = contaBancariaService.obterPorUsuarioId(usuarioId);
        return ResponseEntity.ok(contaDTO);
    }

    @GetMapping("/numero/{numeroConta}")
    public ResponseEntity<ContaBancariaDTO> obterPorNumeroConta(@PathVariable String numeroConta) {
        ContaBancariaDTO contaDTO = contaBancariaService.obterPorNumeroConta(numeroConta);
        return ResponseEntity.ok(contaDTO);
    }

    @GetMapping("/saldo/{usuarioId}")
    public ResponseEntity<Map<String, Object>> obterSaldo(@PathVariable Long usuarioId) {
        ContaBancariaDTO contaDTO = contaBancariaService.obterSaldo(usuarioId);
        Map<String, Object> response = new HashMap<>();
        response.put("saldo", contaDTO.getSaldo());
        response.put("limiteCheque", contaDTO.getLimiteCheque());
        response.put("saldoDisponivel", contaDTO.getSaldo().add(contaDTO.getLimiteCheque()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/depositar/{usuarioId}")
    public ResponseEntity<Map<String, String>> depositar(@PathVariable Long usuarioId, @RequestParam BigDecimal valor) {
        contaBancariaService.depositar(usuarioId, valor);
        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Depósito realizado com sucesso");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sacar/{usuarioId}")
    public ResponseEntity<Map<String, String>> sacar(@PathVariable Long usuarioId, @RequestParam BigDecimal valor) {
        contaBancariaService.sacar(usuarioId, valor);
        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Saque realizado com sucesso");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/limite-cheque/{usuarioId}")
    public ResponseEntity<Map<String, String>> atualizarLimiteCheque(@PathVariable Long usuarioId, @RequestParam BigDecimal novoLimite) {
        contaBancariaService.atualizarLimiteCheque(usuarioId, novoLimite);
        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Limite de cheque atualizado com sucesso");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/desativar/{usuarioId}")
    public ResponseEntity<Map<String, String>> desativarConta(@PathVariable Long usuarioId) {
        contaBancariaService.desativarConta(usuarioId);
        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Conta desativada com sucesso");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/ativar/{usuarioId}")
    public ResponseEntity<Map<String, String>> ativarConta(@PathVariable Long usuarioId) {
        contaBancariaService.ativarConta(usuarioId);
        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Conta ativada com sucesso");
        return ResponseEntity.ok(response);
    }
}

