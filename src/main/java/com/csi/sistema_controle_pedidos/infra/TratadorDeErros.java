package com.csi.sistema_controle_pedidos.infra;

import com.csi.sistema_controle_pedidos.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

@RestControllerAdvice
public class TratadorDeErros {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiError> tratarResponseStatus(ResponseStatusException ex, HttpServletRequest req) {
    HttpStatus status = ex.getStatusCode() instanceof HttpStatus http ? http : HttpStatus.BAD_REQUEST;
    String mensagem = ex.getReason() != null ? ex.getReason() : status.getReasonPhrase();

    ApiError body = ApiError.of(
            status.value(),
            status.name(),
            mensagem,
            req.getRequestURI(),
            null
    );
    return ResponseEntity.status(status).body(body);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiError> onDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
    Throwable root = getRootCause(ex);
    boolean isUnique = hasSqlState(root, "23505");
    String nice = buildNiceMessageForUnique(root);

    String msg = isUnique
            ? (nice != null ? nice : "Violação de unicidade.")
            : "Violação de integridade de dados.";

    ApiError body = ApiError.of(
            HttpStatus.CONFLICT.value(),
            "CONFLICT",
            msg,
            req.getRequestURI(),
            null
    );
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> tratarErro400Validacao(MethodArgumentNotValidException ex, HttpServletRequest req) {
    Map<String,String> fields = new LinkedHashMap<>();
    for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
      fields.put(fe.getField(), fe.getDefaultMessage());
    }
    ApiError body = ApiError.of(
            HttpStatus.BAD_REQUEST.value(),
            "BAD_REQUEST",
            "Dados inválidos",
            req.getRequestURI(),
            fields
    );
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiError> tratarErro400TipoParametro(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
    String paramName = ex.getName();
    String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido";

    String msg = "O parâmetro de URL '%s' é inválido. Esperava-se um valor do tipo '%s'.".formatted(paramName, requiredType);

    ApiError body = ApiError.of(
            HttpStatus.BAD_REQUEST.value(),
            "BAD_REQUEST",
            msg,
            req.getRequestURI(),
            null
    );
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ApiError> tratarErro404(NoSuchElementException ex, HttpServletRequest req) {
    ApiError body = ApiError.of(
            HttpStatus.NOT_FOUND.value(),
            "NOT_FOUND",
            "Recurso não encontrado",
            req.getRequestURI(),
            null
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiError> tratarErro403(AccessDeniedException ex, HttpServletRequest req) {
    ApiError body = ApiError.of(
            HttpStatus.FORBIDDEN.value(),
            "FORBIDDEN",
            "Acesso negado",
            req.getRequestURI(),
            null
    );
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiError> tratarErro401(AuthenticationException ex, HttpServletRequest req) {
    ApiError body = ApiError.of(
            HttpStatus.UNAUTHORIZED.value(),
            "UNAUTHORIZED",
            "Token ausente ou inválido",
            req.getRequestURI(),
            null
    );
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> tratarErro500(Exception ex, HttpServletRequest req) {
    ex.printStackTrace();
    ApiError body = ApiError.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "INTERNAL_SERVER_ERROR",
            "Erro interno. Se persistir, contate o suporte.",
            req.getRequestURI(),
            null
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  //métodos úteis
  private static Throwable getRootCause(Throwable t) {
    Throwable c = t;
    while (c.getCause() != null) c = c.getCause();
    return c;
  }

  private static boolean hasSqlState(Throwable cause, String sqlState) {
    try {
      var m = cause.getClass().getMethod("getSQLState");
      var state = (String) m.invoke(cause);
      return sqlState.equals(state);
    } catch (Exception ignored) {
      return false;
    }
  }

  private static String buildNiceMessageForUnique(Throwable cause) {
    String s = cause.getMessage();
    if (s == null) return null;
    var m = Pattern.compile("Chave \\(([^)]+)\\)=\\(([^)]+)\\)").matcher(s);
    if (m.find()) {
      String field = m.group(1);
      String value = m.group(2);
      return "Valor já existente: %s=%s".formatted(field, value);
    }
    return null;
  }
}
