package io.septem.tax.model.input;

import lombok.Data;

import java.util.List;

@Data
public class TaxYear {
    private final TaxYearSetup setup;
    private final List<Invoice> invoices;
    private final List<Expense> expenses;
    private final List<Donation> donations;
}
