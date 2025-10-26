// src/main/java/com/csi/sistema_controle_pedidos/api/ApiError.java
package com.csi.sistema_controle_pedidos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

public record ApiError(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
    public static ApiError of(int status, String error, String message, String path, Map<String,String> fieldErrors) {
        return new ApiError(OffsetDateTime.now(), status, error, message, path, fieldErrors);
    }
}
