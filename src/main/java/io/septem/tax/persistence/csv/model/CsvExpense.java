package io.septem.tax.persistence.csv.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CsvExpense {

    private final String particulars;
    private final String expenseTypeName;
    private final String invoiceNumber;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final BigDecimal grossValue;
    private final BigDecimal taxableAmount1;
    private final BigDecimal taxPercent1;
    private final BigDecimal taxableAmount2;
    private final BigDecimal taxPercent2;
}
