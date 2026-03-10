package com.bank.api.controller;

import com.bank.api.dto.EmprestimoDTO;
import com.bank.api.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/emprestimos")
@CrossOrigin(origins = "*")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @PostMapping
    public ResponseEntity<EmprestimoDTO> solicitarEmprestimo(
            @RequestParam Long usuarioId,
            @RequestParam BigDecimal valorTotal,
            @RequestParam BigDecimal taxaJuros,
            @RequestParam Integer numeroParcelas) {
        EmprestimoDTO emprestimoDTO = emprestimoService.solicitarEmprestimo(usuarioId, valorTotal, taxaJuros, numeroParcelas);
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoDTO> obterPorId(@PathVariable Long id) {
        EmprestimoDTO emprestimoDTO = emprestimoService.obterPorId(id);
        return ResponseEntity.ok(emprestimoDTO);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EmprestimoDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<EmprestimoDTO> emprestimos = emprestimoService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/ativos/{usuarioId}")
    public ResponseEntity<List<EmprestimoDTO>> listarAtivos(@PathVariable Long usuarioId) {
        List<EmprestimoDTO> emprestimos = emprestimoService.listarAtivos(usuarioId);
        return ResponseEntity.ok(emprestimos);
    }

    @PostMapping("/{id}/aprovar")
    public ResponseEntity<EmprestimoDTO> aprovarEmprestimo(@PathVariable Long id) {
        EmprestimoDTO emprestimoDTO = emprestimoService.aprovarEmprestimo(id);
        return ResponseEntity.ok(emprestimoDTO);
    }

    @PostMapping("/{id}/rejeitar")
    public ResponseEntity<EmprestimoDTO> rejeitarEmprestimo(@PathVariable Long id) {
        EmprestimoDTO emprestimoDTO = emprestimoService.rejeitarEmprestimo(id);
        return ResponseEntity.ok(emprestimoDTO);
    }

    @PostMapping("/{id}/pagamento")
    public ResponseEntity<EmprestimoDTO> realizarPagamento(
            @PathVariable Long id,
            @RequestParam BigDecimal valor) {
        EmprestimoDTO emprestimoDTO = emprestimoService.realizarPagamento(id, valor);
        return ResponseEntity.ok(emprestimoDTO);
    }
}

