package gilvando.vieira.pcas;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.repository.HospitalRepository;
import gilvando.vieira.pcas.service.HospitalService;

@ExtendWith(MockitoExtension.class)
public class HospitalServiceTest {

    @Mock
    HospitalRepository hospitalRepository;

    private HospitalService hospitalService;

    @BeforeEach
    public void setUp() {
        hospitalService = new HospitalService(hospitalRepository);

    }

    @Test
    public void hospitalPorId_returnsHospital() {
        given(hospitalRepository.findById(Hospital.builder().id(1l).build()))
                .willReturn(Hospital.builder().id(1l).nome("Hospital Guadalupe").build());

        Hospital hospital = hospitalService.hospitalPorId(1l);

        assertThat(hospital.getId()).isEqualTo(1l);
        assertThat(hospital.getNome()).isEqualTo("Hospital Guadalupe");
    }

}
