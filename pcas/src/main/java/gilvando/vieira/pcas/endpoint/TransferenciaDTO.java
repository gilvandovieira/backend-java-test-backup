package gilvando.vieira.pcas.endpoint;

import gilvando.vieira.pcas.entity.Hospital;
import gilvando.vieira.pcas.entity.Recurso;
import lombok.Data;

@Data
public class TransferenciaDTO {

    Hospital hospitalRecebe;
    Hospital hospitalEnvia;
    Recurso recursoRecebe;
    Recurso recursoEnvia;
}
