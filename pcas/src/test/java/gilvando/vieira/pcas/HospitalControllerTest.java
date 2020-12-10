package gilvando.vieira.pcas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gilvando.vieira.pcas.endpoint.CapacidadeDTO;
import gilvando.vieira.pcas.endpoint.PacientesDTO;
import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.service.HospitalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.LinkedList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@WebAppConfiguration
public class HospitalControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private WebApplicationContext wac;

  @MockBean private HospitalService hospitalService;

  @BeforeEach
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void retornaListaVazia() throws Exception {
    when(this.hospitalService.listaHospitais()).thenReturn(new LinkedList<Hospital>());

    this.mockMvc
        .perform(get("/hospitais"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
  }

  @Test
  public void retornaUmResultado_quandoEstaArmazenado() throws Exception {
    when(this.hospitalService.listaHospitais())
        .thenReturn(Arrays.asList(Hospital.builder().id(1l).build()));

    this.mockMvc
        .perform(get("/hospitais"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("[0].id").value(1));
  }

  @Test
  public void retornaHospitalPorId_quandoEstaArmazenado() throws Exception {
    when(this.hospitalService.hospitalPorId(1l)).thenReturn(Hospital.builder().id(1l).build());

    this.mockMvc
        .perform(get("/hospitais/1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value(1));
  }

  @Test
  public void retornaNenhumHospitalPorId_quandoNaoEstaArmazenado() throws Exception {
    when(this.hospitalService.hospitalPorId(any())).thenReturn(null);

    this.mockMvc
        .perform(get("/hospitais/{id}", "1"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void criaNovoHospital() throws JsonProcessingException, Exception {
    when(this.hospitalService.salvaHospital(any())).thenReturn(Hospital.builder().id(1l).build());

    this.mockMvc
        .perform(
            post("/hospitais")
                .content(
                    new ObjectMapper()
                        .writeValueAsString(Hospital.builder().nome("Hospital 1").build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andReturn();
  }

  @Test
  public void alteraNrDePacientes() throws JsonProcessingException, Exception {
    when(this.hospitalService.hospitalPorId(any())).thenReturn(Hospital.builder().id(1l).build());
    when(this.hospitalService.alterarNrDePacientes(any(), any()))
        .thenReturn(Hospital.builder().id(1l).pacientes(100l).build());

    this.mockMvc
        .perform(
            put("/hospitais/1/pacientes")
                .content(
                    new ObjectMapper()
                        .writeValueAsString(PacientesDTO.builder().pacientes(100l).build()))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  public void alteraCapacidade() throws JsonProcessingException, Exception {
    when(this.hospitalService.hospitalPorId(any())).thenReturn(Hospital.builder().id(1l).build());
    when(this.hospitalService.alterarCapacidade(any(), any()))
        .thenReturn(Hospital.builder().id(1l).capacidade(100l).build());

    this.mockMvc
        .perform(
            put("/hospitais/1/capacidade")
                .content(
                    new ObjectMapper()
                        .writeValueAsString(CapacidadeDTO.builder().capacidade(100l).build()))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }
}
