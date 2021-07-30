package io.septem.tax.model.in;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Donation {

    private final String organisation;
    private final String registrationNo;
    private final Period period;
    private final BigDecimal value;
}
