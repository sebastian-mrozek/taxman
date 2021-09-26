package io.septem.tax.model.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Invoice {

    private final String particulars;
    private final String customer;
    private final LocalDate dateIssued;
    private final LocalDate datePaid;
    private final BigDecimal netValue;
    private final BigDecimal gstPercent;
    private final BigDecimal withholdingTaxPercent;

    public final BigDecimal getTotal() {
        BigDecimal gstValue = netValue.multiply(gstPercent).divide(BigDecimal.valueOf(100.0), 3, RoundingMode.HALF_UP);
        return netValue.add(gstValue);
    }

    public final BigDecimal getToPay() {
        BigDecimal withholdingTaxValue = netValue.multiply(withholdingTaxPercent).divide(BigDecimal.valueOf(100.0), 3, RoundingMode.HALF_UP);
        return getTotal().subtract(withholdingTaxValue);
    }

    public final TaxDetail getWithholdingTaxDetail() {
        return new TaxDetail(this.netValue, this.withholdingTaxPercent);
    }

    public final TaxDetail getGstDetail() {
        return new TaxDetail(this.netValue, this.gstPercent);
    }
}
