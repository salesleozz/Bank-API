package com.bank.api.service;

import com.bank.api.dto.ContaBancariaDTO;
import com.bank.api.model.ContaBancaria;
import com.bank.api.model.Usuario;
import com.bank.api.repository.ContaBancariaRepository;
import com.bank.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class ContaBancariaService {

    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ContaBancariaDTO obterPorUsuarioId(Long usuarioId) {
        ContaBancaria contaBancaria = contaBancariaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));
        return converterParaDTO(contaBancaria);
    }

    public ContaBancariaDTO obterPorNumeroConta(String numeroConta) {
        ContaBancaria contaBancaria = contaBancariaRepository.findByNumeroConta(numeroConta)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));
        return converterParaDTO(contaBancaria);
    }

    public ContaBancariaDTO obterSaldo(Long usuarioId) {
        return obterPorUsuarioId(usuarioId);
    }

    @Transactional
    public void depositar(Long usuarioId, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor de depósito deve ser maior que zero");
        }

        ContaBancaria contaBancaria = contaBancariaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));

        contaBancaria.setSaldo(contaBancaria.getSaldo().add(valor));
        contaBancariaRepository.save(contaBancaria);
    }

    @Transactional
    public void sacar(Long usuarioId, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor de saque deve ser maior que zero");
        }

        ContaBancaria contaBancaria = contaBancariaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));

        BigDecimal saldoDisponivel = contaBancaria.getSaldo().add(contaBancaria.getLimiteCheque());

        if (valor.compareTo(saldoDisponivel) > 0) {
            throw new RuntimeException("Saldo insuficiente. Saldo disponível: " + saldoDisponivel);
        }

        contaBancaria.setSaldo(contaBancaria.getSaldo().subtract(valor));
        contaBancariaRepository.save(contaBancaria);
    }

    @Transactional
    public void atualizarLimiteCheque(Long usuarioId, BigDecimal novoLimite) {
        if (novoLimite.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Limite de cheque não pode ser negativo");
        }

        ContaBancaria contaBancaria = contaBancariaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));

        contaBancaria.setLimiteCheque(novoLimite);
        contaBancariaRepository.save(contaBancaria);
    }

    @Transactional
    public void desativarConta(Long usuarioId) {
        ContaBancaria contaBancaria = contaBancariaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));

        contaBancaria.setAtiva(false);
        contaBancariaRepository.save(contaBancaria);
    }

    @Transactional
    public void ativarConta(Long usuarioId) {
        ContaBancaria contaBancaria = contaBancariaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));

        contaBancaria.setAtiva(true);
        contaBancariaRepository.save(contaBancaria);
    }

    private ContaBancariaDTO converterParaDTO(ContaBancaria contaBancaria) {
        return ContaBancariaDTO.builder()
                .id(contaBancaria.getId())
                .usuarioId(contaBancaria.getUsuario().getId())
                .numeroConta(contaBancaria.getNumeroConta())
                .agencia(contaBancaria.getAgencia())
                .saldo(contaBancaria.getSaldo())
                .limiteCheque(contaBancaria.getLimiteCheque())
                .ativa(contaBancaria.getAtiva())
                .dataCriacao(contaBancaria.getDataCriacao())
                .build();
    }
}

