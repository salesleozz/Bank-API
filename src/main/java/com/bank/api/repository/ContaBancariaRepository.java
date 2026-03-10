package com.bank.api.repository;

import com.bank.api.model.ContaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {
    Optional<ContaBancaria> findByNumeroConta(String numeroConta);
    Optional<ContaBancaria> findByUsuarioId(Long usuarioId);
}

