package io.septem.tax.model.input;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseType {

    private final String name;
    private final BigDecimal percentDeductible;
}
