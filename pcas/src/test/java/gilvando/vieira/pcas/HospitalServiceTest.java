package gilvando.vieira.pcas;

import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.entity.HospitalLog;
import gilvando.vieira.pcas.entity.Recurso;
import gilvando.vieira.pcas.repository.HospitalLogRepository;
import gilvando.vieira.pcas.repository.HospitalRepository;
import gilvando.vieira.pcas.repository.RecursoLogRepository;
import gilvando.vieira.pcas.service.HospitalService;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class HospitalServiceTest {

  @Mock HospitalRepository hospitalRepository;

  @Mock HospitalLogRepository hospitalLogRepository;

  @Mock RecursoLogRepository recursoLogRepository;

  private HospitalService hospitalService;

  @BeforeEach
  public void setUp() {
    hospitalService =
        new HospitalService(hospitalRepository, hospitalLogRepository, recursoLogRepository);
  }

  @Test
  public void hospitalPorId_returnsHospital() {
    given(hospitalRepository.findById(anyLong()))
        .willReturn(Optional.of(Hospital.builder().id(1l).nome("Hospital Guadalupe").build()));

    Hospital hospital = hospitalService.hospitalPorId(1l);

    assertThat(hospital.getId()).isEqualTo(1l);
    assertThat(hospital.getNome()).isEqualTo("Hospital Guadalupe");
  }

  @Test
  public void alteraCapacidadeDoHospital() {
    given(hospitalRepository.findById(anyLong()))
        .willReturn(Optional.of(Hospital.builder().build()));
    given(hospitalRepository.save(any()))
        .willReturn(Hospital.builder().id(1l).capacidade(100l).build());

    Hospital hospital = hospitalService.alterarCapacidade(1l, 100l);

    assertThat(hospital.getId()).isEqualTo(1l);
    assertThat(hospital.getCapacidade()).isEqualTo(100l);
  }

  @Test
  public void alteraNrDePacientes() {
    given(hospitalRepository.findById(anyLong()))
        .willReturn(Optional.of(Hospital.builder().build()));
    given(hospitalRepository.save(any()))
        .willReturn(Hospital.builder().id(1l).pacientes(100l).build());

    Hospital hospital = hospitalService.alterarNrDePacientes(1l, 100l);

    assertThat(hospital.getId()).isEqualTo(1l);
    assertThat(hospital.getPacientes()).isEqualTo(100l);
  }

  @Test
  public void realizaTranferenciaEntreDoisHospitais_retornaVerdadeiro() {
    Hospital h1 =
        Hospital.builder()
            .id(1l)
            .pacientes(1l)
            .capacidade(10l)
            .recursos(Recurso.builder().ambulancia(1l).build())
            .build();
    Hospital h2 =
        Hospital.builder()
            .id(2l)
            .pacientes(1l)
            .capacidade(10l)
            .recursos(Recurso.builder().respirador(2l).build())
            .build();
    given(hospitalRepository.findById(1l)).willReturn(Optional.of(h1));
    given(hospitalRepository.findById(2l)).willReturn(Optional.of(h2));
    given(hospitalRepository.save(h1))
        .willReturn(
            Hospital.builder().id(1l).recursos(Recurso.builder().respirador(2l).build()).build());
    given(hospitalRepository.save(h2))
        .willReturn(
            Hospital.builder().id(2l).recursos(Recurso.builder().ambulancia(1l).build()).build());

    boolean verdadeiro =
        hospitalService.realizaTransacaoEntreHospitais(
            1l,
            2l,
            Recurso.builder().ambulancia(1l).build(),
            Recurso.builder().respirador(2l).build());

    assertThat(verdadeiro).isTrue();
  }

  @Test
  public void porcentagemDeHospitaisComMaisDe90Porcento() {
    given(hospitalRepository.findAll())
        .willReturn(
            Arrays.asList(
                Hospital.builder().pacientes(99l).capacidade(100l).build(),
                Hospital.builder().capacidade(10l).pacientes(8l).build()));

    Double porcentagem = hospitalService.porcentagemDeHospitaisComOcupacaoMaiorQue90Porcento();

    assertThat(porcentagem).isEqualTo(0.5d, Offset.offset(0.0001d));
  }

  @Test
  public void porcentagemDeHospitaisComMenosDe90Porcento() {
    given(hospitalRepository.findAll())
        .willReturn(
            Arrays.asList(
                Hospital.builder().pacientes(99l).capacidade(100l).build(),
                Hospital.builder().capacidade(10l).pacientes(8l).build()));

    Double porcentagem = hospitalService.porcentagemDeHospitaisComOcupacaoMenorQue90Porcento();

    assertThat(porcentagem).isEqualTo(0.5d, Offset.offset(0.0001d));
  }

  @Test
  public void mediaDeEquipamentos() {
    given(hospitalRepository.findAll())
        .willReturn(
            Arrays.asList(
                Hospital.builder().recursos(Recurso.builder().ambulancia(10l).build()).build(),
                Hospital.builder().recursos(Recurso.builder().ambulancia(20l).build()).build()));

    Map<String, Double> medias = hospitalService.mediasDeRecursos();

    assertThat(medias.get("ambulancia")).isEqualTo(15, Offset.offset(0.0001));
  }

  @Test
  public void registraLogDeAcontecimentos() {
    Hospital h1 = Hospital.builder().id(1l).build();
    given(hospitalRepository.save(any())).willReturn(h1);
    given(hospitalRepository.findById(anyLong())).willReturn(Optional.of(h1));
    given(hospitalLogRepository.save(any())).willReturn(HospitalLog.builder().hospital(h1).build());
    HospitalLog hl1 = HospitalLog.builder().id(1l).hospital(h1).capacidade(100l).build();
    given(hospitalLogRepository.findAll()).willReturn(Arrays.asList(hl1));

    Hospital hospital = this.hospitalService.alterarCapacidade(1l, 100l);

    List<HospitalLog> logs = this.hospitalService.listaLogHospital();

    assertThat(hospital.getCapacidade()).isEqualTo(100l);

    assertThat(logs.size()).isEqualTo(1);
    assertThat(logs.get(0)).isEqualTo(hl1);
  }
}
