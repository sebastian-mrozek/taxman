package io.septem.tax.model.in;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class Invoice {

    private final String particulars;
    private final String customer;
    private final LocalDate dateIssued;
    private final BigDecimal netValue;
    private final GstDetail gstDetail;
    private final BigDecimal withholdingTaxPercent;
}
