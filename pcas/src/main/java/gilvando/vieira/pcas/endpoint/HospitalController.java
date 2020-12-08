package gilvando.vieira.pcas.endpoint;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.service.HospitalService;

@RestController
@RequestMapping(path = "/hospitais")
public class HospitalController {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping
    public List<Hospital> listaHospitais() {

        return this.hospitalService.listaHospitais();
    }

    @GetMapping(path = "/{id}")
    public Hospital retornaHospitalPorId(@PathVariable(name = "id") Long id) {

        Hospital hospital = this.hospitalService.hospitalPorId(id);

        if (hospital == null) {
            throw new HospitalNaoEncontrado();
        }

        return hospital;
    }
}
