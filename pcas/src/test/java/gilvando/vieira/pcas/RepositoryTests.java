package gilvando.vieira.pcas;

import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.entity.HospitalLog;
import gilvando.vieira.pcas.entity.Recurso;
import gilvando.vieira.pcas.repository.HospitalLogRepository;
import gilvando.vieira.pcas.repository.HospitalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// @RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTests {

  @Autowired TestEntityManager testEntityManager;
  @Autowired private HospitalRepository hospitalRepository;
  @Autowired private HospitalLogRepository hospitalLogRepository;

  @Test
  public void dadosEstaoPersistindo() {
    Hospital hospitalSalvo =
        testEntityManager.persistAndFlush(
            Hospital.builder()
                .nome("Hospital Guadalupe")
                .endereco("Av Ruy Carneiro, 1")
                .cnpj("00000000/0000")
                .latitude(-35.0)
                .longitude(-25.0)
                .capacidade(1000l)
                .pacientes(100l)
                .recursos(
                    Recurso.builder()
                        .ambulancia(10l)
                        .enfermeiro(30l)
                        .medico(20l)
                        .respirador(12l)
                        .tomografo(2l)
                        .build())
                .build());
    Hospital hospital = this.hospitalRepository.findByNome("Hospital Guadalupe");

    assertThat(hospital.getCnpj()).isEqualTo(hospitalSalvo.getCnpj());
    assertThat(hospital.getCapacidade()).isEqualTo(1000l);
  }

  @Test
  public void retornaListaDeHospitaisComMaisDeNoventaPorCentoDeOcupacao() {
    Hospital hospitalSalvo =
        testEntityManager.persistAndFlush(
            Hospital.builder()
                .nome("Hospital Guadalupe")
                .endereco("Av Ruy Carneiro, 1")
                .cnpj("00000000/0000")
                .latitude(-35.0)
                .longitude(-25.0)
                .capacidade(1000l)
                .pacientes(900l)
                .recursos(
                    Recurso.builder()
                        .ambulancia(10l)
                        .enfermeiro(30l)
                        .medico(20l)
                        .respirador(12l)
                        .tomografo(2l)
                        .build())
                .build());
    List<Hospital> hospitais =
        this.hospitalRepository.hospitaisComCapacidadeAcimaDeNoventaPorCento();

    assertThat(hospitais.size()).isEqualTo(1);
    assertThat(hospitais.get(0).getNome()).isEqualTo("Hospital Guadalupe");
  }

  @Test
  public void retornaTodosLogs() {
    Hospital hospital = testEntityManager.persistAndFlush(Hospital.builder().build());
    HospitalLog hospitalLog =
        testEntityManager.persistAndFlush(
            HospitalLog.builder()
                .dataHora(LocalDateTime.now())
                .hospital(Hospital.builder().id(1l).build())
                .build());

    List<HospitalLog> hospitalLogs = hospitalLogRepository.findAll();

    assertThat(hospitalLogs.size()).isEqualTo(1);
    assertThat(hospitalLogs.get(0)).isEqualTo(hospitalLog);
  }
}
