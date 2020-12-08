package gilvando.vieira.pcas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gilvando.vieira.pcas.entity.Hospital;

@Repository
public interface HospitalRepository extends CrudRepository<Hospital, Long> {

    Hospital findById(Hospital hospital);

    Hospital findByNome(String nome);

    List<Hospital> findAll();

    // Ordem importa, primeiro o valor depois o calculo
    @Query("SELECT h FROM Hospital h WHERE (0.9 >= (h.pacientes/h.capacidade))")
    List<Hospital> hospitaisComCapacidadeAcimaDeNoventaPorCento();
}
