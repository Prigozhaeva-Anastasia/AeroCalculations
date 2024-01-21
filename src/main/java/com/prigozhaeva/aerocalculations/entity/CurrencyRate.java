package com.prigozhaeva.aerocalculations.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@Builder
public class CurrencyRate {
    private String numCode;
    private String charCode;
    private String nominal;
    private String name;
    private BigDecimal value;
}
