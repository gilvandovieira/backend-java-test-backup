package gilvando.vieira.pcas.service;

import org.springframework.stereotype.Service;

import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.entity.Recurso;
import gilvando.vieira.pcas.entity.RecursoExcedidoException;
import gilvando.vieira.pcas.repository.HospitalRepository;

@Service
public class GestorService {

    private HospitalRepository hospitalRepository;

    public GestorService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public boolean realizaTransacaoEntreHospitais(Long h1, Long h2, Recurso aReceber, Recurso aEnviar) {

        Hospital hospitalAReceber = this.hospitalRepository.findById(Hospital.builder().id(h1).build());
        Hospital hospitalAEnviar = this.hospitalRepository.findById(Hospital.builder().id(h2).build());

        if (hospitalAReceber.lotacao() >= 0.9) {

            try {
                hospitalAEnviar.getRecursos().subtrai(aEnviar);
            } catch (RecursoExcedidoException e) {
                e.printStackTrace();
                return false;
            }

            hospitalAReceber.getRecursos().soma(aReceber);

            this.hospitalRepository.save(hospitalAEnviar);
            this.hospitalRepository.save(hospitalAReceber);
            return true;
        }

        if (aReceber.equivalente(aEnviar)) {
            try {
                hospitalAEnviar.getRecursos().subtrai(aEnviar);
            } catch (RecursoExcedidoException e) {
                e.printStackTrace();
                return false;
            }
            hospitalAReceber.getRecursos().soma(aReceber);

            this.hospitalRepository.save(hospitalAEnviar);
            this.hospitalRepository.save(hospitalAReceber);
            return true;
        }

        return false;

    }

}
