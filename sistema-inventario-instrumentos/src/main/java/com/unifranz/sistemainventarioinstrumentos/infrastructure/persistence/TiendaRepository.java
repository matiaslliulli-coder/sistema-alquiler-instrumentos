package com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence;

import com.unifranz.sistemainventarioinstrumentos.domain.Tienda;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TiendaRepository extends JpaRepository<Tienda, Integer> {

    @Query("select t from Tienda t join fetch t.ciudad where t.estado = 'A' order by t.nombreTienda")
    List<Tienda> findActivasConCiudad();

    @Query("select t from Tienda t join fetch t.ciudad where t.codTienda = :cod")
    Optional<Tienda> findConCiudadById(@Param("cod") Integer cod);
}
