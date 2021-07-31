package io.septem.tax.model.output;

import io.septem.tax.model.input.Expense;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ExpenseClaim {

    private final Expense expense;
    private final BigDecimal claimValue;
    private final BigDecimal claimPercent;
}
