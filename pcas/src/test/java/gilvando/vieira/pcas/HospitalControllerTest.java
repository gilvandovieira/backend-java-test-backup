package gilvando.vieira.pcas;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.service.HospitalService;

@WebMvcTest
public class HospitalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HospitalService hospitalService;

    @Test
    public void retornaListaVazia() throws Exception {
        when(this.hospitalService.listaHospitais()).thenReturn(new LinkedList<Hospital>());

        this.mockMvc.perform(get("/hospitais")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void retornaUmResultado_quandoEstaArmazenado() throws Exception {
        when(this.hospitalService.listaHospitais()).thenReturn(List.of(Hospital.builder().id(1l).build()));

        this.mockMvc.perform(get("/hospitais")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("[0].id").value(1));
    }

    @Test
    public void retornaHospitalPorId_quandoEstaArmazenado() throws Exception {
        when(this.hospitalService.hospitalPorId(1l)).thenReturn(Hospital.builder().id(1l).build());

        this.mockMvc.perform(get("/hospitais/1")).andExpect(status().isOk()).andExpect(jsonPath("id").value(1));
    }

    @Test
    public void retornaNenhumHospitalPorId_quandoNaoEstaArmazenado() throws Exception {
        when(this.hospitalService.hospitalPorId(any())).thenReturn(null);

        this.mockMvc.perform(get("/hospitais/{id}", "1")).andExpect(status().isNotFound());
    }
}
