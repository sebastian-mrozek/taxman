package io.septem.tax.model.in;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IncomeTaxRate {

    private final BigDecimal above;
    private final BigDecimal rate;
}
