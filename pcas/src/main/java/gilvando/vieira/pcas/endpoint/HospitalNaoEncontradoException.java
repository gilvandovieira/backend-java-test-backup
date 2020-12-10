package gilvando.vieira.pcas.endpoint;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HospitalNaoEncontradoException extends RuntimeException {

  /** */
  private static final long serialVersionUID = 3151611839129793999L;

  public HospitalNaoEncontradoException() {
    super();
  }

  public HospitalNaoEncontradoException(String msg) {
    super(msg);
  }
}
