package io.septem.tax.model.input;


import lombok.Data;

import java.util.List;

@Data
public class TaxYearSetup {
    private final String label;
    private final List<Period> gstReturnPeriods;
    private final List<ExpenseType> expenseTypes;
    private final List<IncomeTaxRate> incomeTaxRates;
}
