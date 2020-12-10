package gilvando.vieira.pcas.service;

import gilvando.vieira.pcas.endpoint.HospitalNaoEncontradoException;
import gilvando.vieira.pcas.entity.*;
import gilvando.vieira.pcas.repository.HospitalLogRepository;
import gilvando.vieira.pcas.repository.HospitalRepository;
import gilvando.vieira.pcas.repository.RecursoLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalLogRepository hospitalLogRepository;
    private final RecursoLogRepository recursoLogRepository;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository, HospitalLogRepository hospitalLogRepository, RecursoLogRepository recursoLogRepository) {
        this.hospitalRepository = hospitalRepository;
        this.hospitalLogRepository = hospitalLogRepository;
        this.recursoLogRepository = recursoLogRepository;
    }

    public List<Hospital> listaHospitais() {

        return this.hospitalRepository.findAll();
    }

    public List<HospitalLog> listaLogHospital() {
        return this.hospitalLogRepository.findAll();
    }

    public List<RecursoLog> listaLogRecurso() {
        return this.recursoLogRepository.findAll();
    }

    public Hospital hospitalPorId(Long id) {
        Optional<Hospital> optHospital = this.hospitalRepository.findById(id);

        Hospital hospital = optHospital.orElseThrow((() -> new HospitalNaoEncontradoException()));

        return hospital;

    }

    public HospitalLog registraLog(Hospital hospital) {
        return this.hospitalLogRepository.save(
                HospitalLog.builder().dataHora(LocalDateTime.now()).hospital(hospital).pacientes(hospital.getPacientes()).capacidade(hospital.getCapacidade()).build());
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
        Hospital h = this.hospitalRepository.save(hospital);
        this.registraLog(hospital);
        return h;
    }

    public void registraLogDeRecursos(Hospital h1, Hospital h2, Recurso r1, Recurso r2) {
        this.recursoLogRepository.save(RecursoLog.builder().dataHora(LocalDateTime.now()).total(r1.soma(r2)).enviadoHospital(h1).recebidoHospital(h2).build());
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
            registraLogDeRecursos(hospitalAReceber, hospitalAEnviar, aReceber, aEnviar);
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
            registraLogDeRecursos(hospitalAReceber, hospitalAEnviar, aReceber, aEnviar);
            return true;
        }

        return false;

    }

    public Double porcentagemDeHospitaisComOcupacaoMaiorQue90Porcento() {
        List<Hospital> hospitais = this.listaHospitais();
        Long count = hospitais.stream().count();
        Long nrHospitaisComMaisDe90Porcento = hospitais.stream().filter((hospital -> (hospital.getPacientes().doubleValue() / hospital.getCapacidade().doubleValue()) >= 0.9d)).count();


        return Double.valueOf(nrHospitaisComMaisDe90Porcento.doubleValue() / count.doubleValue());
    }

    public Double porcentagemDeHospitaisComOcupacaoMenorQue90Porcento() {
        List<Hospital> hospitais = this.listaHospitais();
        Long count = hospitais.stream().count();

        Long nrHospitaisComMenosDe90Porcento = hospitais.stream().filter((hospital -> (hospital.getPacientes().doubleValue() / hospital.getCapacidade().doubleValue()) < 0.9)).count();

        return nrHospitaisComMenosDe90Porcento.doubleValue() / count.doubleValue();
    }

    public Map<String, Double> mediasDeRecursos() {
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

    public Hospital superLotacaoAMaisTempo() {
        List<HospitalLog> hospitalLogs = this.listaLogHospital();

        Map<Long, Double> mediaLotacaoPorHospital = hospitalLogs.stream()
                .filter(hospitalLog -> hospitalLog.lotacao() >= 0.9)
                .collect(
                        Collectors
                                .groupingBy(hospitalLog -> hospitalLog.getHospital().getId(),
                                        Collectors.averagingDouble(value -> value.getDataHora().toEpochSecond(ZoneOffset.UTC))));

        return this.hospitalPorId(mediaLotacaoPorHospital.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey());
    }

    public Hospital baixaLotacaoAMaisTempo() {
        List<HospitalLog> hospitalLogs = this.listaLogHospital();

        Map<Long, Double> mediaLotacaoPorHospital = hospitalLogs.stream()
                .filter(hospitalLog -> hospitalLog.lotacao() < 0.9)
                .collect(
                        Collectors
                                .groupingBy(hospitalLog -> hospitalLog.getHospital().getId(),
                                        Collectors.averagingDouble(value -> value.getDataHora().toEpochSecond(ZoneOffset.UTC))));

        return this.hospitalPorId(mediaLotacaoPorHospital.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey());
    }

}
