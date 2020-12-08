package gilvando.vieira.pcas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gilvando.vieira.pcas.endpoint.HospitalNaoEncontradoException;
import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.repository.HospitalRepository;

@Service
public class HospitalService {

    private HospitalRepository hospitalRepository;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public List<Hospital> listaHospitais() {

        return this.hospitalRepository.findAll();
    }

    public Hospital hospitalPorId(Long id) {
        Hospital hospital = this.hospitalRepository.findById(Hospital.builder().id(id).build());

        if (hospital == null) {
            throw new HospitalNaoEncontradoException();
        }

        return hospital;

    }

    /**
     * 
     * @param id        identificador do hospital
     * @param pacientes altera o total de pacientes no hospital
     * @return hospital atualizado
     */
    public Hospital alterarNrDePacientes(Long id, Long pacientes) {

        Hospital hospital = this.hospitalRepository.findById(Hospital.builder().id(id).build());

        if (hospital == null) {
            throw new HospitalNaoEncontradoException();
        }

        hospital.setPacientes(pacientes);
        return this.hospitalRepository.save(hospital);

    }

    /**
     * 
     * @param id         identificador do hospital
     * @param capacidade capacidade total do hospital
     * @return hospital atualizado
     */
    public Hospital alterarCapacidade(Long id, Long capacidade) {

        Hospital hospital = this.hospitalRepository.findById(Hospital.builder().id(id).build());

        if (hospital == null) {
            throw new HospitalNaoEncontradoException();
        }

        hospital.setCapacidade(capacidade);
        return this.hospitalRepository.save(hospital);

    }
    /**
     * Adiciona novo hospital ao Banco de dados
     * @param hospital
     * @return Retorna entidade hospital
     */
    public Hospital salvaHospital(Hospital hospital) {
       // hospital.setId(null);
        return this.hospitalRepository.save(hospital);
    }
}
