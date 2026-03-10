package com.bank.api.service;

import com.bank.api.dto.EmprestimoDTO;
import com.bank.api.model.Emprestimo;
import com.bank.api.model.StatusEmprestimo;
import com.bank.api.model.Usuario;
import com.bank.api.model.ContaBancaria;
import com.bank.api.repository.EmprestimoRepository;
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
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    @Transactional
    public EmprestimoDTO solicitarEmprestimo(Long usuarioId, BigDecimal valorTotal, BigDecimal taxaJuros, Integer numeroParcelas) {

        if (valorTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor deve ser maior que zero");
        }

        if (numeroParcelas <= 0) {
            throw new RuntimeException("Número de parcelas deve ser maior que zero");
        }

        if (taxaJuros.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Taxa de juros não pode ser negativa");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setValorTotal(valorTotal);
        emprestimo.setSaldoDevedor(valorTotal);
        emprestimo.setTaxaJuros(taxaJuros);
        emprestimo.setNumeroParcelas(numeroParcelas);
        emprestimo.setParcelasQuitadas(0);
        emprestimo.setStatusEmprestimo(StatusEmprestimo.EM_ANALISE);
        emprestimo.setDataContratacao(LocalDateTime.now());
        emprestimo.setDataPrimeiroVencimento(LocalDateTime.now().plusMonths(1));
        emprestimo.setDataProximoVencimento(LocalDateTime.now().plusMonths(1));

        Emprestimo emprestimoSalvo = emprestimoRepository.save(emprestimo);
        return converterParaDTO(emprestimoSalvo);
    }

    @Transactional
    public EmprestimoDTO aprovarEmprestimo(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        if (!emprestimo.getStatusEmprestimo().equals(StatusEmprestimo.EM_ANALISE)) {
            throw new RuntimeException("Apenas empréstimos em análise podem ser aprovados");
        }

        emprestimo.setStatusEmprestimo(StatusEmprestimo.APROVADO);

        // Depositar valor na conta do usuário
        ContaBancaria contaBancaria = contaBancariaRepository.findByUsuarioId(emprestimo.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));

        contaBancaria.setSaldo(contaBancaria.getSaldo().add(emprestimo.getValorTotal()));
        contaBancariaRepository.save(contaBancaria);

        Emprestimo emprestimoAtualizado = emprestimoRepository.save(emprestimo);
        return converterParaDTO(emprestimoAtualizado);
    }

    @Transactional
    public EmprestimoDTO rejeitarEmprestimo(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        emprestimo.setStatusEmprestimo(StatusEmprestimo.REJEITADO);
        Emprestimo emprestimoAtualizado = emprestimoRepository.save(emprestimo);
        return converterParaDTO(emprestimoAtualizado);
    }

    public EmprestimoDTO obterPorId(Long id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));
        return converterParaDTO(emprestimo);
    }

    public List<EmprestimoDTO> listarPorUsuario(Long usuarioId) {
        return emprestimoRepository.findByUsuarioIdOrderByDataContracacaoDesc(usuarioId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<EmprestimoDTO> listarAtivos(Long usuarioId) {
        return emprestimoRepository.findByStatusEmprestimoAndUsuarioId(StatusEmprestimo.APROVADO, usuarioId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmprestimoDTO realizarPagamento(Long emprestimoId, BigDecimal valor) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        if (!emprestimo.getStatusEmprestimo().equals(StatusEmprestimo.APROVADO)) {
            throw new RuntimeException("Apenas empréstimos aprovados podem receber pagamentos");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor de pagamento deve ser maior que zero");
        }

        if (valor.compareTo(emprestimo.getSaldoDevedor()) > 0) {
            throw new RuntimeException("Valor de pagamento maior que o saldo devedor");
        }

        emprestimo.setSaldoDevedor(emprestimo.getSaldoDevedor().subtract(valor));

        if (emprestimo.getSaldoDevedor().compareTo(BigDecimal.ZERO) <= 0) {
            emprestimo.setParcelasQuitadas(emprestimo.getNumeroParcelas());
            emprestimo.setStatusEmprestimo(StatusEmprestimo.QUITADO);
            emprestimo.setSaldoDevedor(BigDecimal.ZERO);
        } else {
            emprestimo.setParcelasQuitadas(emprestimo.getParcelasQuitadas() + 1);
            emprestimo.setDataProximoVencimento(emprestimo.getDataProximoVencimento().plusMonths(1));
        }

        Emprestimo emprestimoAtualizado = emprestimoRepository.save(emprestimo);
        return converterParaDTO(emprestimoAtualizado);
    }

    private EmprestimoDTO converterParaDTO(Emprestimo emprestimo) {
        return EmprestimoDTO.builder()
                .id(emprestimo.getId())
                .usuarioId(emprestimo.getUsuario().getId())
                .valorTotal(emprestimo.getValorTotal())
                .saldoDevedor(emprestimo.getSaldoDevedor())
                .taxaJuros(emprestimo.getTaxaJuros())
                .numeroParcelas(emprestimo.getNumeroParcelas())
                .parcelasQuitadas(emprestimo.getParcelasQuitadas())
                .statusEmprestimo(emprestimo.getStatusEmprestimo().toString())
                .dataContratacao(emprestimo.getDataContratacao())
                .dataPrimeiroVencimento(emprestimo.getDataPrimeiroVencimento())
                .dataProximoVencimento(emprestimo.getDataProximoVencimento())
                .build();
    }
}

