package io.septem.tax.persistence.csv.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CsvInvoice {

    private final String customer;
    private final LocalDate dateIssued;
    private final BigDecimal netValue;
    private final String particulars;
    private final BigDecimal gstPercent;
    private final BigDecimal withholdingTaxPercent;
    private final LocalDate datePaid;
}
