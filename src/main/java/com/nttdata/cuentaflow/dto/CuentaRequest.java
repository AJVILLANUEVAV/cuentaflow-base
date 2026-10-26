package com.nttdata.cuentaflow.dto;

import com.nttdata.cuentaflow.model.Moneda;
import com.nttdata.cuentaflow.model.TipoCuenta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/** Cuerpo del POST /api/v1/cuentas. Las anotaciones aplican la regla RN-01. */
public record CuentaRequest(
        @NotBlank(message = "el DNI es obligatorio")
        @Pattern(regexp = "\\d{8}", message = "el DNI debe tener exactamente 8 digitos")
        String dni,
        @NotBlank(message = "el titular es obligatorio")
        String titular,
        @NotNull(message = "el tipo de cuenta es obligatorio")
        TipoCuenta tipo,
        @NotNull(message = "la moneda es obligatoria")
        Moneda moneda,
        @NotNull(message = "el saldo inicial es obligatorio")
        @PositiveOrZero(message = "el saldo inicial no puede ser negativo")
        BigDecimal saldoInicial) {
}
