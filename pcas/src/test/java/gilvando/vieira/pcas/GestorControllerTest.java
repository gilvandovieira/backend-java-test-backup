package gilvando.vieira.pcas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gilvando.vieira.pcas.endpoint.TransferenciaDTO;
import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.entity.Recurso;
import gilvando.vieira.pcas.service.HospitalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
public class GestorControllerTest {

  @Autowired MockMvc mockMvc;
  @MockBean HospitalService hospitalService;
  @Autowired private WebApplicationContext wac;

  @BeforeEach
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void tentaFazerUmaTransferencia_RealizadaComSucesso()
      throws JsonProcessingException, Exception {
    //
    // given(hospitalRepository.findById(Hospital.builder().id(1l).build())).willReturn(Hospital.builder().id(1l)
    //                .recursos(
    //
    // Recurso.builder().medico(3l).ambulancia(3l).enfermeiro(3l).respirador(3l).tomografo(3l).build())
    //                .build());
    when(hospitalService.hospitalPorId(1l))
        .thenReturn(
            Hospital.builder()
                .id(1l)
                .capacidade(10l)
                .pacientes(2l)
                .recursos(
                    Recurso.builder()
                        .medico(3l)
                        .ambulancia(3l)
                        .enfermeiro(3l)
                        .respirador(3l)
                        .tomografo(3l)
                        .build())
                .build());

    when(hospitalService.hospitalPorId(2l))
        .thenReturn(
            Hospital.builder()
                .id(2l)
                .capacidade(10l)
                .pacientes(2l)
                .recursos(
                    Recurso.builder()
                        .medico(3l)
                        .ambulancia(3l)
                        .enfermeiro(3l)
                        .respirador(3l)
                        .tomografo(3l)
                        .build())
                .build());

    when(hospitalService.realizaTransacaoEntreHospitais(
            1l,
            2l,
            Recurso.builder().ambulancia(1l).build(),
            Recurso.builder().respirador(2l).build()))
        .thenReturn(true);

    mockMvc
        .perform(
            post("/gestao/transferencia")
                .content(
                    new ObjectMapper()
                        .writeValueAsString(
                            TransferenciaDTO.builder()
                                .hospitalEnvia(2l)
                                .hospitalRecebe(1l)
                                .recursoEnvia(Recurso.builder().respirador(2l).build())
                                .recursoRecebe(Recurso.builder().ambulancia(1l).build())
                                .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void tentaFazerUmaTransferencia_RealizadaComFalha()
      throws JsonProcessingException, Exception {
    when(hospitalService.hospitalPorId(1l))
        .thenReturn(
            Hospital.builder()
                .id(1l)
                .capacidade(10l)
                .pacientes(2l)
                .recursos(
                    Recurso.builder()
                        .medico(3l)
                        .ambulancia(3l)
                        .enfermeiro(3l)
                        .respirador(3l)
                        .tomografo(3l)
                        .build())
                .build());

    when(hospitalService.hospitalPorId(2l))
        .thenReturn(
            Hospital.builder()
                .id(2l)
                .capacidade(10l)
                .pacientes(2l)
                .recursos(
                    Recurso.builder()
                        .medico(3l)
                        .ambulancia(3l)
                        .enfermeiro(3l)
                        .respirador(3l)
                        .tomografo(3l)
                        .build())
                .build());

    when(hospitalService.realizaTransacaoEntreHospitais(
            1l,
            2l,
            Recurso.builder().ambulancia(2l).build(),
            Recurso.builder().respirador(2l).build()))
        .thenReturn(true);

    mockMvc
        .perform(
            post("/gestao/transferencia")
                .content(
                    new ObjectMapper()
                        .writeValueAsString(
                            TransferenciaDTO.builder()
                                .hospitalEnvia(2l)
                                .hospitalRecebe(1l)
                                .recursoEnvia(Recurso.builder().respirador(2l).build())
                                .recursoRecebe(Recurso.builder().ambulancia(1l).build())
                                .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }
}
