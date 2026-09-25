package com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence;

import com.unifranz.sistemainventarioinstrumentos.domain.Instrumento;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface InstrumentoRepository extends JpaRepository<Instrumento, String>, JpaSpecificationExecutor<Instrumento> {

    /** Siguiente numero de la secuencia usada para generar los codigos INS-000000. */
    @Query(value = "select nextval('seq_cod_instrumento')", nativeQuery = true)
    Long siguienteNumero();

    @EntityGraph(attributePaths = {"subcategoria", "subcategoria.categoria", "marca", "tienda", "tienda.ciudad"})
    Optional<Instrumento> findByCodInstrumento(String codInstrumento);

    /** Busqueda con filtros dinamicos; carga de una vez las relaciones que necesita la respuesta. */
    @Override
    @EntityGraph(attributePaths = {"subcategoria", "subcategoria.categoria", "marca", "tienda", "tienda.ciudad"})
    List<Instrumento> findAll(Specification<Instrumento> spec, Sort sort);
}
