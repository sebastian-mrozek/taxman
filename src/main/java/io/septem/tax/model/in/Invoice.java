package io.septem.tax.model.in;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Data
@Builder
public class Invoice {

    private final String particulars;
    private final String customer;
    private final LocalDate dateIssued;
    private final BigDecimal netValue;
    private final TaxDetail gstDetail;
    private final BigDecimal withholdingTaxPercent;

    public final BigDecimal getTotal() {
        return netValue.add(gstDetail.getTaxValue());
    }

    public final BigDecimal getToPay() {
        BigDecimal withholdingTaxValue = netValue.multiply(withholdingTaxPercent).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        return getTotal().subtract(withholdingTaxValue);
    }

    public final TaxDetail getWithholdingTaxDetail() {
        return new TaxDetail(this.netValue, this.withholdingTaxPercent);
    }
}
