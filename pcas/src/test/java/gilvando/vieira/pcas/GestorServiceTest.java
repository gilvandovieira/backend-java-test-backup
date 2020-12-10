package gilvando.vieira.pcas;

import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.entity.HospitalLog;
import gilvando.vieira.pcas.entity.Recurso;
import gilvando.vieira.pcas.repository.HospitalLogRepository;
import gilvando.vieira.pcas.repository.HospitalRepository;
import gilvando.vieira.pcas.repository.RecursoLogRepository;
import gilvando.vieira.pcas.service.HospitalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GestorServiceTest {

  @Mock HospitalRepository hospitalRepository;

  @Mock HospitalLogRepository hospitalLogRepository;

  @Mock RecursoLogRepository recursoLogRepository;
  Recurso r;
  private HospitalService hospitalService;

  @BeforeEach
  public void setUp() {
    hospitalService =
        new HospitalService(hospitalRepository, hospitalLogRepository, recursoLogRepository);
    r =
        Recurso.builder()
            .medico(3l)
            .ambulancia(3l)
            .enfermeiro(3l)
            .respirador(3l)
            .tomografo(3l)
            .build();
  }

  @Test
  public void dadoHospitaisComOcupacaoNormal_TrocaDeRecursosTemQueSerEquivalente() {

    given(hospitalRepository.findById(2l))
        .willReturn(
            Optional.of(
                Hospital.builder().id(2l).capacidade(10l).pacientes(8l).recursos(r).build()));
    given(hospitalRepository.findById(3l))
        .willReturn(
            Optional.of(
                Hospital.builder().id(3l).capacidade(10l).pacientes(7l).recursos(r).build()));

    boolean verdade =
        hospitalService.realizaTransacaoEntreHospitais(
            2l,
            3l,
            Recurso.builder().ambulancia(1l).build(),
            Recurso.builder().respirador(2l).build());

    assertThat(verdade).isTrue();
  }

  @Test
  public void dadoHospitaisComOcupacaoNormal_TrocaDeRecursosNaoEquivalente() {

    given(hospitalRepository.findById(2l))
        .willReturn(
            Optional.of(
                Hospital.builder().id(2l).capacidade(10l).pacientes(8l).recursos(r).build()));
    given(hospitalRepository.findById(3l))
        .willReturn(
            Optional.of(
                Hospital.builder().id(3l).capacidade(10l).pacientes(7l).recursos(r).build()));

    boolean verdade =
        hospitalService.realizaTransacaoEntreHospitais(
            2l,
            3l,
            Recurso.builder().ambulancia(1l).build(),
            Recurso.builder().respirador(1l).build());

    assertThat(verdade).isFalse();
  }

  @Test
  public void dadoHospitalComOcupacaoPreocupante_TrocaDeRecursosNaoPrecisaSerEquivalente() {
    given(hospitalRepository.findById(1l))
        .willReturn(
            Optional.of(
                Hospital.builder().id(1l).capacidade(10l).pacientes(9l).recursos(r).build()));
    given(hospitalRepository.findById(3l))
        .willReturn(
            Optional.of(
                Hospital.builder().id(3l).capacidade(10l).pacientes(7l).recursos(r).build()));

    boolean verdade =
        hospitalService.realizaTransacaoEntreHospitais(
            1l, 3l, Recurso.builder().ambulancia(1l).build(), Recurso.builder().build());
    assertThat(verdade).isTrue();
  }

  @Test
  public void determinaSeUmHospitalEstaAMaisTempoComSuperLotacao() {
    given(hospitalLogRepository.findAll())
        .willReturn(
            List.of(
                HospitalLog.builder()
                    .capacidade(100l)
                    .pacientes(90l)
                    .dataHora(LocalDateTime.now())
                    .hospital(Hospital.builder().id(1l).build())
                    .build(),
                HospitalLog.builder()
                    .hospital(Hospital.builder().id(2l).build())
                    .capacidade(100l)
                    .pacientes(89l)
                    .dataHora(LocalDateTime.now())
                    .build()));

    given(hospitalRepository.findById(1l))
        .willReturn(Optional.of(Hospital.builder().id(1l).build()));

    Hospital hospital = hospitalService.superLotacaoAMaisTempo();

    assertThat(hospital.getId()).isEqualTo(1l);
  }

  @Test
  public void determinaSeUmHospitalEstaAMaisTempoComSuperLotacao_2() {
    given(hospitalLogRepository.findAll())
        .willReturn(
            List.of(
                HospitalLog.builder()
                    .capacidade(100l)
                    .pacientes(90l)
                    .dataHora(LocalDateTime.now())
                    .hospital(Hospital.builder().id(1l).build())
                    .build(),
                HospitalLog.builder()
                    .hospital(Hospital.builder().id(2l).build())
                    .capacidade(100l)
                    .pacientes(89l)
                    .dataHora(LocalDateTime.now())
                    .build(),
                HospitalLog.builder()
                    .hospital(Hospital.builder().id(2l).build())
                    .dataHora(LocalDateTime.now().plusDays(3l))
                    .capacidade(100l)
                    .pacientes(90l)
                    .build(),
                HospitalLog.builder()
                    .hospital(Hospital.builder().id(1l).build())
                    .capacidade(100l)
                    .pacientes(91l)
                    .dataHora(LocalDateTime.now().plusDays(55l))
                    .build()));

    given(hospitalRepository.findById(anyLong()))
        .willReturn(Optional.of(Hospital.builder().id(1l).build()));

    Hospital hospital = hospitalService.superLotacaoAMaisTempo();

    assertThat(hospital.getId()).isEqualTo(1l);
  }

  @Test
  public void determinaSeUmHospitalEstaAMaisTempoComBaixaLotacao() {
    given(hospitalLogRepository.findAll())
        .willReturn(
            List.of(
                HospitalLog.builder()
                    .capacidade(100l)
                    .pacientes(90l)
                    .dataHora(LocalDateTime.now())
                    .hospital(Hospital.builder().id(1l).build())
                    .build(),
                HospitalLog.builder()
                    .hospital(Hospital.builder().id(2l).build())
                    .capacidade(100l)
                    .pacientes(89l)
                    .dataHora(LocalDateTime.now())
                    .build()));

    given(hospitalRepository.findById(anyLong()))
        .willReturn(Optional.of(Hospital.builder().id(2l).build()));

    Hospital hospital = hospitalService.baixaLotacaoAMaisTempo();

    assertThat(hospital.getId()).isEqualTo(2l);
  }

  @Test
  public void determinaSeUmHospitalEstaAMaisTempoComBaixaLotacao_2() {
    given(hospitalLogRepository.findAll())
        .willReturn(
            List.of(
                HospitalLog.builder()
                    .capacidade(100l)
                    .pacientes(90l)
                    .dataHora(LocalDateTime.now())
                    .hospital(Hospital.builder().id(1l).build())
                    .build(),
                HospitalLog.builder()
                    .hospital(Hospital.builder().id(2l).build())
                    .capacidade(100l)
                    .pacientes(89l)
                    .dataHora(LocalDateTime.now())
                    .build(),
                HospitalLog.builder()
                    .hospital(Hospital.builder().id(2l).build())
                    .dataHora(LocalDateTime.now().plusDays(3l))
                    .capacidade(100l)
                    .pacientes(89l)
                    .build(),
                HospitalLog.builder()
                    .hospital(Hospital.builder().id(2l).build())
                    .dataHora(LocalDateTime.now().plusDays(4l))
                    .capacidade(100l)
                    .pacientes(89l)
                    .build(),
                HospitalLog.builder()
                    .hospital(Hospital.builder().id(2l).build())
                    .dataHora(LocalDateTime.now().plusDays(5l))
                    .capacidade(100l)
                    .pacientes(91l)
                    .build(),
                HospitalLog.builder()
                    .hospital(Hospital.builder().id(1l).build())
                    .capacidade(100l)
                    .pacientes(89l)
                    .dataHora(LocalDateTime.now().plusDays(3l))
                    .build()));

    given(hospitalRepository.findById(anyLong()))
        .willReturn(Optional.of(Hospital.builder().id(2l).build()));

    Hospital hospital = hospitalService.baixaLotacaoAMaisTempo();

    assertThat(hospital.getId()).isEqualTo(2l);
  }
}
