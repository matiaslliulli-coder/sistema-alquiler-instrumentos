package com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence;

import com.unifranz.sistemaalquilerinstrumentos.domain.BitacoraAuditoria;
import java.util.List;
import org.springframework.data.repository.Repository;

/** Repositorio de solo-agregar: expone unicamente guardar y consultar (la bitacora es inmutable). */
public interface BitacoraRepository extends Repository<BitacoraAuditoria, Long> {

    BitacoraAuditoria save(BitacoraAuditoria evento);

    List<BitacoraAuditoria> findTop100ByOrderByFechaDesc();

    List<BitacoraAuditoria> findTop100ByAccionOrderByFechaDesc(String accion);
}
