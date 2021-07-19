package io.septem.tax.persistence.csv.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CsvInvoice {

    private final String particulars;
    private final String customer;
    private final LocalDate dateIssued;
    private final LocalDate datePaid;
    private final BigDecimal netValue;
    private final BigDecimal taxableAmount;
    private final BigDecimal taxPercent;
    private final BigDecimal withholdingTaxPercent;

}
