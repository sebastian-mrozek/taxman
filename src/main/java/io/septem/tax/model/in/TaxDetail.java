package io.septem.tax.model.in;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxDetail {

    private final BigDecimal taxableAmount;
    private final BigDecimal taxPercent;

    public BigDecimal getTaxValue() {
        return taxableAmount.multiply(taxPercent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

}
