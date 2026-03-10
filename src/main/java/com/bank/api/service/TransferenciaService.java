package com.bank.api.service;

import com.bank.api.dto.TransferenciaDTO;
import com.bank.api.model.Transferencia;
import com.bank.api.model.TipoTransferencia;
import com.bank.api.model.StatusTransferencia;
import com.bank.api.model.Usuario;
import com.bank.api.model.ContaBancaria;
import com.bank.api.repository.TransferenciaRepository;
import com.bank.api.repository.UsuarioRepository;
import com.bank.api.repository.ContaBancariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransferenciaService {

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    @Transactional
    public TransferenciaDTO realizarTransferencia(Long usuarioOrigemId, Long usuarioDestinoId, BigDecimal valor, String descricao, String tipoTransferencia) {

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor deve ser maior que zero");
        }

        Usuario usuarioOrigem = usuarioRepository.findById(usuarioOrigemId)
                .orElseThrow(() -> new RuntimeException("Usuário de origem não encontrado"));

        Usuario usuarioDestino = usuarioRepository.findById(usuarioDestinoId)
                .orElseThrow(() -> new RuntimeException("Usuário de destino não encontrado"));

        if (usuarioOrigemId.equals(usuarioDestinoId)) {
            throw new RuntimeException("Não é possível transferir para a mesma conta");
        }

        ContaBancaria contaOrigem = contaBancariaRepository.findByUsuarioId(usuarioOrigemId)
                .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada"));

        ContaBancaria contaDestino = contaBancariaRepository.findByUsuarioId(usuarioDestinoId)
                .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada"));

        // Verificar saldo
        BigDecimal saldoDisponivel = contaOrigem.getSaldo().add(contaOrigem.getLimiteCheque());
        if (valor.compareTo(saldoDisponivel) > 0) {
            throw new RuntimeException("Saldo insuficiente. Saldo disponível: " + saldoDisponivel);
        }

        // Débito na conta origem
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaBancariaRepository.save(contaOrigem);

        // Crédito na conta destino
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));
        contaBancariaRepository.save(contaDestino);

        // Registrar transferência
        Transferencia transferencia = new Transferencia();
        transferencia.setUsuarioOrigem(usuarioOrigem);
        transferencia.setUsuarioDestino(usuarioDestino);
        transferencia.setValor(valor);
        transferencia.setDescricao(descricao);
        transferencia.setTipoTransferencia(TipoTransferencia.valueOf(tipoTransferencia.toUpperCase()));
        transferencia.setStatusTransferencia(StatusTransferencia.CONCLUIDA);
        transferencia.setDataTransferencia(LocalDateTime.now());

        Transferencia transferenciaSalva = transferenciaRepository.save(transferencia);
        return converterParaDTO(transferenciaSalva);
    }

    public TransferenciaDTO obterPorId(Long id) {
        Transferencia transferencia = transferenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transferência não encontrada"));
        return converterParaDTO(transferencia);
    }

    public List<TransferenciaDTO> listarEnviadas(Long usuarioId) {
        return transferenciaRepository.findByUsuarioOrigemIdOrderByDataTransferenciaDesc(usuarioId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<TransferenciaDTO> listarRecebidas(Long usuarioId) {
        return transferenciaRepository.findByUsuarioDestinoIdOrderByDataTransferenciaDesc(usuarioId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<TransferenciaDTO> listarTodas(Long usuarioId) {
        List<Transferencia> enviadas = transferenciaRepository.findByUsuarioOrigemIdOrderByDataTransferenciaDesc(usuarioId);
        List<Transferencia> recebidas = transferenciaRepository.findByUsuarioDestinoIdOrderByDataTransferenciaDesc(usuarioId);

        List<Transferencia> todas = enviadas;
        todas.addAll(recebidas);

        return todas.stream()
                .map(this::converterParaDTO)
                .sorted((t1, t2) -> t2.getDataTransferencia().compareTo(t1.getDataTransferencia()))
                .collect(Collectors.toList());
    }

    private TransferenciaDTO converterParaDTO(Transferencia transferencia) {
        return TransferenciaDTO.builder()
                .id(transferencia.getId())
                .usuarioOrigemId(transferencia.getUsuarioOrigem().getId())
                .usuarioDestinoId(transferencia.getUsuarioDestino().getId())
                .usuarioOrigemNome(transferencia.getUsuarioOrigem().getNome())
                .usuarioDestinoNome(transferencia.getUsuarioDestino().getNome())
                .valor(transferencia.getValor())
                .descricao(transferencia.getDescricao())
                .tipoTransferencia(transferencia.getTipoTransferencia().toString())
                .statusTransferencia(transferencia.getStatusTransferencia().toString())
                .dataTransferencia(transferencia.getDataTransferencia())
                .dataCriacao(transferencia.getDataCriacao())
                .build();
    }
}

