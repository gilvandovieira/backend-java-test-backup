package gilvando.vieira.pcas.service;

import gilvando.vieira.pcas.repository.HospitalRepository;


public class GestorService {

    private HospitalRepository hospitalRepository;

    public GestorService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    

}
