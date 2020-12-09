package gilvando.vieira.pcas.repository;

import gilvando.vieira.pcas.entity.RecursoLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecursoLogRepository extends CrudRepository<RecursoLog, Long> {
    List<RecursoLog> findAll();
}
