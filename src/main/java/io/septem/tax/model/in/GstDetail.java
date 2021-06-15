package io.septem.tax.model.in;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GstDetail {

    private final BigDecimal taxableAmount;
    private final BigDecimal taxPercent;

}
