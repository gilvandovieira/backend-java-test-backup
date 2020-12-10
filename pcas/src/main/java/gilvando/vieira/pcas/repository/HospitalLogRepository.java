package gilvando.vieira.pcas.repository;

import gilvando.vieira.pcas.entity.HospitalLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalLogRepository extends CrudRepository<HospitalLog, Long> {

  List<HospitalLog> findAll();

  List<HospitalLog> findAllByHospital(Long hospital);
}
