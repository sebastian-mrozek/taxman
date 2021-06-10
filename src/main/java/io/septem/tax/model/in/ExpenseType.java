package io.septem.tax.model.in;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseType {

    private final String name;
    private final BigDecimal percentageDeductible;
}
