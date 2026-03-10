package com.bank.api.service;

import com.bank.api.dto.CartaoDTO;
import com.bank.api.model.Cartao;
import com.bank.api.model.StatusCartao;
import com.bank.api.model.TipoCartao;
import com.bank.api.model.Usuario;
import com.bank.api.repository.CartaoRepository;
import com.bank.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public CartaoDTO criar(Long usuarioId, String titular, String dataValidade, BigDecimal limiteCredito, String tipoCartao) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Cartao cartao = new Cartao();
        cartao.setUsuario(usuario);
        cartao.setNumeroCartao(gerarNumeroCartao());
        cartao.setTitular(titular);
        cartao.setDataValidade(dataValidade);
        cartao.setCvv(gerarCVV());
        cartao.setLimiteCredito(limiteCredito);
        cartao.setSaldoDisponivel(limiteCredito);
        cartao.setFaturaAtual(BigDecimal.ZERO);
        cartao.setStatusCartao(StatusCartao.ATIVO);
        cartao.setTipoCartao(TipoCartao.valueOf(tipoCartao.toUpperCase()));

        Cartao cartaoSalvo = cartaoRepository.save(cartao);
        return converterParaDTO(cartaoSalvo);
    }

    public CartaoDTO obterPorId(Long id) {
        Cartao cartao = cartaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));
        return converterParaDTO(cartao);
    }

    public CartaoDTO obterPorNumeroCartao(String numeroCartao) {
        Cartao cartao = cartaoRepository.findByNumeroCartao(numeroCartao)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));
        return converterParaDTO(cartao);
    }

    public List<CartaoDTO> listarPorUsuario(Long usuarioId) {
        return cartaoRepository.findByUsuarioIdOrderByDataCriacaoDesc(usuarioId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CartaoDTO bloquearCartao(Long cartaoId) {
        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        cartao.setStatusCartao(StatusCartao.BLOQUEADO);
        Cartao cartaoAtualizado = cartaoRepository.save(cartao);
        return converterParaDTO(cartaoAtualizado);
    }

    @Transactional
    public CartaoDTO desbloquearCartao(Long cartaoId) {
        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        cartao.setStatusCartao(StatusCartao.ATIVO);
        Cartao cartaoAtualizado = cartaoRepository.save(cartao);
        return converterParaDTO(cartaoAtualizado);
    }

    @Transactional
    public CartaoDTO cancelarCartao(Long cartaoId) {
        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        cartao.setStatusCartao(StatusCartao.CANCELADO);
        Cartao cartaoAtualizado = cartaoRepository.save(cartao);
        return converterParaDTO(cartaoAtualizado);
    }

    @Transactional
    public CartaoDTO realizarCompra(Long cartaoId, BigDecimal valor, String descricao) {
        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        if (!cartao.getStatusCartao().equals(StatusCartao.ATIVO)) {
            throw new RuntimeException("Cartão não está ativo");
        }

        if (valor.compareTo(cartao.getSaldoDisponivel()) > 0) {
            throw new RuntimeException("Limite insuficiente. Saldo disponível: " + cartao.getSaldoDisponivel());
        }

        cartao.setSaldoDisponivel(cartao.getSaldoDisponivel().subtract(valor));
        cartao.setFaturaAtual(cartao.getFaturaAtual().add(valor));

        Cartao cartaoAtualizado = cartaoRepository.save(cartao);
        return converterParaDTO(cartaoAtualizado);
    }

    @Transactional
    public CartaoDTO pagarFatura(Long cartaoId, BigDecimal valor) {
        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor de pagamento deve ser maior que zero");
        }

        if (valor.compareTo(cartao.getFaturaAtual()) > 0) {
            throw new RuntimeException("Valor de pagamento maior que a fatura");
        }

        cartao.setFaturaAtual(cartao.getFaturaAtual().subtract(valor));
        cartao.setSaldoDisponivel(cartao.getSaldoDisponivel().add(valor));

        Cartao cartaoAtualizado = cartaoRepository.save(cartao);
        return converterParaDTO(cartaoAtualizado);
    }

    private String gerarNumeroCartao() {
        StringBuilder numero = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            numero.append((int) (Math.random() * 10));
        }
        return numero.toString();
    }

    private String gerarCVV() {
        StringBuilder cvv = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            cvv.append((int) (Math.random() * 10));
        }
        return cvv.toString();
    }

    private CartaoDTO converterParaDTO(Cartao cartao) {
        return CartaoDTO.builder()
                .id(cartao.getId())
                .usuarioId(cartao.getUsuario().getId())
                .numeroCartao(cartao.getNumeroCartao())
                .titular(cartao.getTitular())
                .dataValidade(cartao.getDataValidade())
                .cvv(cartao.getCvv())
                .limiteCredito(cartao.getLimiteCredito())
                .saldoDisponivel(cartao.getSaldoDisponivel())
                .faturaAtual(cartao.getFaturaAtual())
                .statusCartao(cartao.getStatusCartao().toString())
                .tipoCartao(cartao.getTipoCartao().toString())
                .dataCriacao(cartao.getDataCriacao())
                .build();
    }
}

