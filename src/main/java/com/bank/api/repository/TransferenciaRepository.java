package com.bank.api.repository;

import com.bank.api.model.Transferencia;
import com.bank.api.model.StatusTransferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    List<Transferencia> findByUsuarioOrigemIdOrderByDataTransferenciaDesc(Long usuarioOrigemId);
    List<Transferencia> findByUsuarioDestinoIdOrderByDataTransferenciaDesc(Long usuarioDestinoId);
    List<Transferencia> findByStatusTransferencia(StatusTransferencia statusTransferencia);
    List<Transferencia> findByUsuarioOrigemIdAndStatusTransferenciaOrderByDataTransferenciaDesc(Long usuarioOrigemId, StatusTransferencia status);
}

