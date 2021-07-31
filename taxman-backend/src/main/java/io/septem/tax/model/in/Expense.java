package io.septem.tax.model.in;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Expense {

    private final String particulars;
    private final String expenseTypeName;
    private final String invoiceNumber;
    private final Period period;
    private final BigDecimal grossValue;
    private final List<TaxDetail> gstDetails;
    private final BigDecimal netValue;
    private final String description;

    public BigDecimal getNetValue() {
        if (gstDetails == null || gstDetails.isEmpty()) {
            return grossValue;
        } else {
            TaxDetail taxDetail = gstDetails.get(0);
            return grossValue.subtract(taxDetail.getTaxValue());
        }
    }
}
