package gilvando.vieira.pcas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gilvando.vieira.pcas.endpoint.HospitalNaoEncontrado;
import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.repository.HospitalRepository;

@Service
public class HospitalService {

    private HospitalRepository hospitalRepository;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }


    public List<Hospital> listaHospitais(){
        
        return this.hospitalRepository.findAll();
    }

    public Hospital hospitalPorId(Long id) {
        Hospital hospital = this.hospitalRepository.findById(Hospital.builder().id(id).build());

        if (hospital == null) {
            throw new HospitalNaoEncontrado();
        }

        return hospital;

    }
}
