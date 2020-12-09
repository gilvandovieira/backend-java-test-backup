package gilvando.vieira.pcas;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.entity.Recurso;
import gilvando.vieira.pcas.repository.HospitalRepository;
import gilvando.vieira.pcas.service.HospitalService;

@ExtendWith(MockitoExtension.class)
public class GestorServiceTest {

        @Mock
        HospitalRepository hospitalRepository;

        private HospitalService gestorService;
        Recurso r;

        @BeforeEach
        public void setUp() {
                gestorService = new HospitalService(hospitalRepository);
                r = Recurso.builder().medico(3l).ambulancia(3l).enfermeiro(3l).respirador(3l).tomografo(3l).build();

        }

        @Test
        public void dadoHospitaisComOcupacaoNormal_TrocaDeRecursosTemQueSerEquivalente() {

                given(hospitalRepository.findById(Hospital.builder().id(2l).build())).willReturn(
                                Hospital.builder().id(2l).capacidade(10l).pacientes(8l).recursos(r).build());
                given(hospitalRepository.findById(Hospital.builder().id(3l).build())).willReturn(
                                Hospital.builder().id(3l).capacidade(10l).pacientes(7l).recursos(r).build());

                boolean verdade = gestorService.realizaTransacaoEntreHospitais(2l, 3l,
                                Recurso.builder().ambulancia(1l).build(), Recurso.builder().respirador(2l).build());

                assertThat(verdade).isTrue();
        }

        @Test
        public void dadoHospitaisComOcupacaoNormal_TrocaDeRecursosNaoEquivalente() {

                given(hospitalRepository.findById(Hospital.builder().id(2l).build())).willReturn(
                                Hospital.builder().id(2l).capacidade(10l).pacientes(8l).recursos(r).build());
                given(hospitalRepository.findById(Hospital.builder().id(3l).build())).willReturn(
                                Hospital.builder().id(3l).capacidade(10l).pacientes(7l).recursos(r).build());

                boolean verdade = gestorService.realizaTransacaoEntreHospitais(2l, 3l,
                                Recurso.builder().ambulancia(1l).build(), Recurso.builder().respirador(1l).build());

                assertThat(verdade).isFalse();
        }

        @Test
        public void dadoHospitalComOcupacaoPreocupante_TrocaDeRecursosNaoPrecisaSerEquivalente() {
                given(hospitalRepository.findById(Hospital.builder().id(1l).build())).willReturn(
                                Hospital.builder().id(1l).capacidade(10l).pacientes(9l).recursos(r).build());
                given(hospitalRepository.findById(Hospital.builder().id(3l).build())).willReturn(
                                Hospital.builder().id(3l).capacidade(10l).pacientes(7l).recursos(r).build());

                boolean verdade = gestorService.realizaTransacaoEntreHospitais(1l, 3l,
                                Recurso.builder().ambulancia(1l).build(), Recurso.builder().build());
                assertThat(verdade).isTrue();
        }
}
