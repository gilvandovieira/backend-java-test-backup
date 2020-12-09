package gilvando.vieira.pcas.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Builder.Default
    private Long capacidade = 0l;
    @Builder.Default
    private Long pacientes = 0l;

    @OneToMany(mappedBy = "hospital")
    private List<HospitalLog> logs;

    public Double lotacao() {
        return this.pacientes.doubleValue() / this.capacidade.doubleValue();
    }

}
