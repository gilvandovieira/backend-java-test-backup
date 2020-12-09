package gilvando.vieira.pcas.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RecursoLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "recebido_hospital")
    private Hospital recebidoHospital;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "enviado_hospital")
    private Hospital enviadoHospital;


    @Embedded
    private Recurso total;


    LocalDateTime dataHora;

}
