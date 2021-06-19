package io.septem.tax.model.in;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class TaxDetail {

    private final BigDecimal taxableAmount;
    private final BigDecimal taxPercent;

    public BigDecimal getTaxValue() {
        return taxableAmount.multiply(taxPercent).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
    }

}
