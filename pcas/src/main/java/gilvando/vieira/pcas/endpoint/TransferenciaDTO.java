package gilvando.vieira.pcas.endpoint;

import gilvando.vieira.pcas.entity.Recurso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaDTO {

  Long hospitalRecebe;
  Long hospitalEnvia;
  Recurso recursoRecebe;
  Recurso recursoEnvia;
}
