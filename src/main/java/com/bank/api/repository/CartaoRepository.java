package com.bank.api.repository;

import com.bank.api.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    Optional<Cartao> findByNumeroCartao(String numeroCartao);
    List<Cartao> findByUsuarioId(Long usuarioId);
    List<Cartao> findByUsuarioIdOrderByDataCriacaoDesc(Long usuarioId);
}

