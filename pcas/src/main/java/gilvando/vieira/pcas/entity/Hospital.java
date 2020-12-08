package gilvando.vieira.pcas.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome, endereco, cnpj;

    private Double longitude, latitude;

    @Embedded
    private Recurso recursos;

    private Long capacidade, pacientes;

    public Double lotacao() {
        return this.pacientes.doubleValue() / this.capacidade.doubleValue();
    }

}
