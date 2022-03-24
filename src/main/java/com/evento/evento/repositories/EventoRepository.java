package com.evento.evento.repositories;

import com.evento.evento.models.Evento;
import org.springframework.data.repository.CrudRepository;

public interface EventoRepository extends CrudRepository<Evento,String> {
    Evento findByCodigo(long codigo);
}
