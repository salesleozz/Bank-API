package com.bank.api.repository;

import com.bank.api.model.Emprestimo;
import com.bank.api.model.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByUsuarioIdOrderByDataContracacaoDesc(Long usuarioId);
    List<Emprestimo> findByStatusEmprestimoAndUsuarioId(StatusEmprestimo statusEmprestimo, Long usuarioId);
    List<Emprestimo> findByStatusEmprestimo(StatusEmprestimo statusEmprestimo);
}

