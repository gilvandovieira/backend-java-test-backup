package gilvando.vieira.pcas.endpoint;

import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/hospitais")
public class HospitalController {

  private final HospitalService hospitalService;

  @Autowired
  public HospitalController(HospitalService hospitalService) {
    this.hospitalService = hospitalService;
  }

  @GetMapping(produces = "application/json")
  public List<Hospital> listaHospitais() {

    return this.hospitalService.listaHospitais();
  }

  @GetMapping(path = "/{id}", produces = "application/json")
  public Hospital retornaHospitalPorId(@PathVariable(name = "id") Long id) {

    Hospital hospital = this.hospitalService.hospitalPorId(id);

    if (hospital == null) {
      throw new HospitalNaoEncontradoException();
    }

    return hospital;
  }

  @PostMapping(produces = "application/json")
  public ResponseEntity<Hospital> adicionaHospital(@RequestBody Hospital entity) {
    Hospital hospital = this.hospitalService.salvaHospital(entity);

    return ResponseEntity.status(HttpStatus.CREATED).body(hospital);
  }

  @PutMapping(
      consumes = "application/json",
      produces = "application/json",
      path = "/{id}/pacientes")
  public ResponseEntity<Hospital> alteraNrDePacientes(
      @PathVariable(name = "id") Long id, @RequestBody(required = true) PacientesDTO pacientesDTO) {

    Hospital hospital = this.hospitalService.alterarNrDePacientes(id, pacientesDTO.getPacientes());

    return ResponseEntity.ok(hospital);
  }

  @PutMapping(
      consumes = "application/json",
      produces = "application/json",
      path = "/{id}/capacidade")
  public ResponseEntity<Hospital> alteraCapacidade(
      @PathVariable(name = "id") Long id, @RequestBody CapacidadeDTO capacidadeDTO) {

    Hospital hospital = this.hospitalService.alterarCapacidade(id, capacidadeDTO.getCapacidade());

    return ResponseEntity.ok(hospital);
  }
}
