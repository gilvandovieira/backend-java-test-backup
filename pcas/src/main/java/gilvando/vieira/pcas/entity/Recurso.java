package gilvando.vieira.pcas.entity;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recurso {

    public static final Long medicoPts = 3l, enfermeiroPts = 3l, respiradorPts = 5l, tomografoPts = 12l,
            ambulanciaPts = 10l;

    @Positive
    private Long medico, enfermeiro, respirador, tomografo, ambulancia;

    public Long pontos() {
        return this.medico * Recurso.medicoPts + this.enfermeiro * Recurso.enfermeiroPts
                + this.respirador * Recurso.respiradorPts + this.tomografo * Recurso.tomografoPts
                + this.ambulancia * Recurso.ambulanciaPts;
    }

    public Long pontos(Long medico, Long enfermeiro, Long respirador, Long tomografo, Long ambulancia) {
        return medico * Recurso.medicoPts + enfermeiro * Recurso.enfermeiroPts + respirador * Recurso.respiradorPts
                + tomografo * Recurso.tomografoPts + ambulancia * Recurso.ambulanciaPts;
    }
}
