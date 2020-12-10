package gilvando.vieira.pcas.endpoint;

import gilvando.vieira.pcas.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/gestao")
public class GestorController {

  private HospitalService hospitalService;

  @Autowired
  public GestorController(HospitalService hospitalService) {
    this.hospitalService = hospitalService;
  }

  @PostMapping(
      value = "/transferencia",
      consumes = "application/json",
      produces = "application/json")
  public ResponseEntity realizaTranferencia(@RequestBody TransferenciaDTO entity) {
    boolean verdade =
        this.hospitalService.realizaTransacaoEntreHospitais(
            entity.hospitalRecebe, entity.hospitalEnvia, entity.recursoRecebe, entity.recursoEnvia);

    if (verdade) {
      return ResponseEntity.ok().build();
    }

    return ResponseEntity.badRequest().body(false);
  }

  @GetMapping(produces = "application/json")
  public ResponseEntity geraRelatorios() {
    Map<String, Object> relatorio = new HashMap<>();

    Double porcentagem_hospitais_com_mais_de_noventa_porcento_de_ocupacao =
        hospitalService.porcentagemDeHospitaisComOcupacaoMaiorQue90Porcento();
    if (!porcentagem_hospitais_com_mais_de_noventa_porcento_de_ocupacao.isNaN())
      relatorio.put(
          "porcentagem_hospitais_com_mais_de_noventa_porcento_de_ocupacao",
          porcentagem_hospitais_com_mais_de_noventa_porcento_de_ocupacao);
    Double porcentagem_hospitais_com_menos_de_noventa_porcento_de_ocupacao =
        hospitalService.porcentagemDeHospitaisComOcupacaoMenorQue90Porcento();
    if (!porcentagem_hospitais_com_menos_de_noventa_porcento_de_ocupacao.isNaN())
      relatorio.put(
          "porcentagem_hospitais_com_menos_de_noventa_porcento_de_ocupacao",
          porcentagem_hospitais_com_menos_de_noventa_porcento_de_ocupacao);

    try {
      relatorio.put("medias", hospitalService.mediasDeRecursos());
    } catch (RuntimeException runtimeException) {
      relatorio.clear();
      relatorio.put("mensagem", runtimeException.getMessage());
      return ResponseEntity.badRequest().body(relatorio);
    }

    relatorio.put("historico_de_negociacao", hospitalService.listaLogRecurso());

    relatorio.put(
        "hospital_com_super_lotacao_a_mais_tempo", hospitalService.superLotacaoAMaisTempo());
    relatorio.put(
        "hospital_abaixo_da_super_lotacao_a_mais_tempo", hospitalService.baixaLotacaoAMaisTempo());

    return ResponseEntity.ok(relatorio);
  }
}
