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

    //@OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, orphanRemoval = true)
    //private List<HospitalLog> logs;

    //@OneToMany(mappedBy = "recebidoHospital", fetch = FetchType.LAZY, orphanRemoval = true)
    //private List<RecursoLog> recursosRecebidos;

    //@OneToMany(mappedBy = "enviadoHospital", fetch = FetchType.LAZY, orphanRemoval = true)
    //private List<RecursoLog> recursosEnviados;

    public Double lotacao() {
        return this.pacientes.doubleValue() / this.capacidade.doubleValue();
    }

}
