package io.septem.tax.model.input;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IncomeTaxRate {

    private final BigDecimal above;
    private final BigDecimal rate;
}
