package gilvando.vieira.pcas.service;

import gilvando.vieira.pcas.endpoint.HospitalNaoEncontradoException;
import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.entity.Recurso;
import gilvando.vieira.pcas.entity.RecursoExcedidoException;
import gilvando.vieira.pcas.repository.HospitalLogRepository;
import gilvando.vieira.pcas.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HospitalService {

    private HospitalRepository hospitalRepository;
    private HospitalLogRepository hospitalLogRepository;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository,HospitalLogRepository hospitalLogRepository) {
        this.hospitalRepository = hospitalRepository;
        this.hospitalLogRepository = hospitalLogRepository;
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
     *
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

            this.salvaHospital(hospitalAEnviar);
            this.salvaHospital(hospitalAReceber);
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

            this.salvaHospital(hospitalAEnviar);
            this.salvaHospital(hospitalAReceber);
            return true;
        }

        return false;

    }

    public Double porcentagemDeHospitaisComOcupacaoMaiorQue90Porcento() {
        List<Hospital> hospitais = this.listaHospitais();
        Long count = hospitais.stream().count();
        Long nrHospitaisComMaisDe90Porcento = hospitais.stream().filter((hospital -> (hospital.getPacientes().doubleValue() / hospital.getCapacidade().doubleValue()) >= 0.9d)).count();
        //long nrHospitaisComMenosDe90Porcento = hospitais.stream().filter((hospital -> (hospital.getPacientes() / hospital.getCapacidade()) < 0.9)).count();

        return Double.valueOf(nrHospitaisComMaisDe90Porcento.doubleValue() / count.doubleValue());
    }

    public Double porcentagemDeHospitaisComOcupacaoMenorQue90Porcento() {
        List<Hospital> hospitais = this.listaHospitais();
        Long count = hospitais.stream().count();

        Long nrHospitaisComMenosDe90Porcento = hospitais.stream().filter((hospital -> (hospital.getPacientes().doubleValue() / hospital.getCapacidade().doubleValue()) < 0.9)).count();

        return nrHospitaisComMenosDe90Porcento.doubleValue() / count.doubleValue();
    }

    public Map<String, Double> mediaDeEquipamentos(){
        List<Hospital> hospitais = this.listaHospitais();

        OptionalDouble optAmbulancia = hospitais.stream().mapToLong(value -> value.getRecursos().getAmbulancia()).average();
        OptionalDouble optEnfermeiro = hospitais.stream().mapToLong(value -> value.getRecursos().getEnfermeiro()).average();
        OptionalDouble optMedico = hospitais.stream().mapToLong(value -> value.getRecursos().getMedico()).average();
        OptionalDouble optRespirador = hospitais.stream().mapToLong(value -> value.getRecursos().getRespirador()).average();
        OptionalDouble optTomografo = hospitais.stream().mapToLong(value -> value.getRecursos().getTomografo()).average();

        Double ambulancia = optAmbulancia.orElseThrow(() -> new RuntimeException("Não pode realizar a média de ambulancias."));
        Double enfermeiro = optEnfermeiro.orElseThrow(() -> new RuntimeException("Não pode realizar a média de enfermeiros."));
        Double medicos = optMedico.orElseThrow(() -> new RuntimeException("Não pode realizar a média de médicos."));
        Double respirador = optRespirador.orElseThrow(() -> new RuntimeException("Não pode realizar a média de respiradores."));
        Double tomografo = optTomografo.orElseThrow(() -> new RuntimeException("Não pode realizar a média de tomógrafos."));

        Map<String, Double> medias = new HashMap<>();
        medias.put("ambulancia", ambulancia);
        medias.put("enfermeiro", enfermeiro);
        medias.put("medicos", medicos);
        medias.put("respirador", respirador);
        medias.put("tomografo", tomografo);
        return medias;
    }
}
