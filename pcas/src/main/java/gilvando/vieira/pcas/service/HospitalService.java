package gilvando.vieira.pcas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gilvando.vieira.pcas.endpoint.HospitalNaoEncontradoException;
import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.entity.Recurso;
import gilvando.vieira.pcas.entity.RecursoExcedidoException;
import gilvando.vieira.pcas.repository.HospitalRepository;

@Service
public class HospitalService {

    private HospitalRepository hospitalRepository;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public List<Hospital> listaHospitais() {

        return this.hospitalRepository.findAll();
    }

    public Hospital hospitalPorId(Long id) {
        Optional<Hospital> optHospital = this.hospitalRepository.findById(id);

        Hospital hospital = optHospital.orElseThrow((() -> new HospitalNaoEncontradoException()));

        return hospital;

    }

    /**
     * 
     * @param id        identificador do hospital
     * @param pacientes altera o total de pacientes no hospital
     * @return hospital atualizado
     */
    public Hospital alterarNrDePacientes(Long id, Long pacientes) {

        Hospital hospital = this.hospitalPorId(id);

        if (hospital == null) {
            throw new HospitalNaoEncontradoException("Hospital não encontrado. Não foi possível alterar número de pacientes.");
        }

        hospital.setPacientes(pacientes);
        return this.salvaHospital(hospital);

    }

    /**
     * 
     * @param id         identificador do hospital
     * @param capacidade capacidade total do hospital
     * @return hospital atualizado
     */
    public Hospital alterarCapacidade(Long id, Long capacidade) {

        Hospital hospital = this.hospitalPorId(id);

        if (hospital == null) {
            throw new HospitalNaoEncontradoException();
        }

        hospital.setCapacidade(capacidade);
        return this.salvaHospital(hospital);

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

    public boolean realizaTransacaoEntreHospitais(Long h1, Long h2, Recurso aReceber, Recurso aEnviar) {

        Hospital hospitalAReceber = this.hospitalPorId(h1);
        Hospital hospitalAEnviar = this.hospitalPorId(h2);

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
