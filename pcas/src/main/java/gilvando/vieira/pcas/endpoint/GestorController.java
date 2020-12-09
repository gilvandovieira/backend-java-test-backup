package gilvando.vieira.pcas.endpoint;

import gilvando.vieira.pcas.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/gestao")
public class GestorController {

    private HospitalService hospitalService;

    @Autowired
    public GestorController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PostMapping(value = "/transferencia")
    public ResponseEntity realizaTranferencia(@RequestBody TransferenciaDTO entity) {
        boolean verdade = this.hospitalService.realizaTransacaoEntreHospitais(entity.hospitalRecebe,
                entity.hospitalEnvia, entity.recursoRecebe, entity.recursoEnvia);

        if (verdade) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body(false);
    }

}
