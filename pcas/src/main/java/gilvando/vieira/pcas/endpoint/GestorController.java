package gilvando.vieira.pcas.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gilvando.vieira.pcas.service.GestorService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/gestao")
public class GestorController {

    GestorService gestorService;

    public GestorController(GestorService gs) {
        this.gestorService = gs;
    }

    @PostMapping(value = "/transferencia")
    public ResponseEntity realizaTranferencia(@RequestBody TransferenciaDTO entity) {
        boolean verdade = this.gestorService.realizaTransacaoEntreHospitais(entity.hospitalRecebe.getId(),
                entity.hospitalEnvia.getId(), entity.recursoRecebe, entity.recursoEnvia);

        if (verdade) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

}
