package io.septem.tax.model.input;

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
    private final List<TaxDetail> gstDetails;
    private final String description;

    public BigDecimal getNetValue() {
        return gstDetails.stream().map(TaxDetail::getNetValue)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getGrossValue() {
        return gstDetails.stream().map(TaxDetail::getGrossValue)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }
}
