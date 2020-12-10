package gilvando.vieira.pcas.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "hospital_id")
  private Hospital hospital;

  private LocalDateTime dataHora;

  @Builder.Default private Long capacidade = 0l;
  @Builder.Default private Long pacientes = 0l;

  public Double lotacao() {
    return pacientes.doubleValue() / capacidade.doubleValue();
  }
}
