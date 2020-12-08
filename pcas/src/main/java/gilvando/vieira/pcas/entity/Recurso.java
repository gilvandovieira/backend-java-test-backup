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

    @Builder.Default
    private Long medico = 0l;
    @Builder.Default
    private Long enfermeiro = 0l;
    @Builder.Default
    private Long respirador = 0l;
    @Builder.Default
    private Long tomografo = 0l;
    @Builder.Default
    private Long ambulancia = 0l;

    public Long pontos() {
        Long total = 0l;
        if (!(this.medico == null))
            total += this.medico * Recurso.medicoPts;
        if (!(this.enfermeiro == null))
            total += this.enfermeiro * Recurso.enfermeiroPts;
        if (!(this.respirador == null))
            total += this.respirador * Recurso.respiradorPts;
        if (!(this.tomografo == null))
            total += this.tomografo * Recurso.tomografoPts;
        if (!(this.ambulancia == null))
            total += this.ambulancia * Recurso.ambulanciaPts;
        return total;
    }

    public boolean equivalente(Recurso recurso) {
        return this.pontos() == recurso.pontos();
    }

    public void soma(Recurso recurso) {
        this.ambulancia += recurso.ambulancia;
        this.enfermeiro += recurso.enfermeiro;
        this.medico += recurso.medico;
        this.respirador += recurso.respirador;
        this.tomografo += recurso.tomografo;
    }

    public void subtrai(Recurso recurso) throws RecursoExcedidoException {
        if (this.ambulancia < recurso.ambulancia)
            throw new RecursoExcedidoException("Nr de ambulancia excedida.");
        this.ambulancia -= recurso.ambulancia;
        if (this.enfermeiro < recurso.enfermeiro)
            throw new RecursoExcedidoException("Nr de enfermeiro excedido.");
        this.enfermeiro -= recurso.enfermeiro;
        if (this.medico < recurso.medico)
            throw new RecursoExcedidoException("Nr de medicos excedido.");
        this.medico -= recurso.medico;
        if (this.respirador < recurso.respirador)
            throw new RecursoExcedidoException("Nr de respiradores excedido.");
        this.respirador -= recurso.respirador;
        if (this.tomografo < recurso.tomografo)
            throw new RecursoExcedidoException("Nr de tomografos excedido.");
        this.tomografo -= recurso.tomografo;
    }
}
