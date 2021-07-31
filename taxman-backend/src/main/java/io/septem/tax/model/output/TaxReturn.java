package io.septem.tax.model.output;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.septem.tax.model.input.Invoice;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxReturn {

    private final String year;
    private final BigDecimal incomeTax;

    private final List<Invoice> invoices;
    private final List<ExpenseClaim> expenseClaims;
    private final List<GstReturn> gstReturns;

    private final BigDecimal profit;
    private final BigDecimal totalExpensesClaim;
    private final BigDecimal netIncome;
    private final BigDecimal incomeTaxPaid;
    private final BigDecimal remainingIncomeTax;
    private final BigDecimal effectiveIncomeTaxPercent;
}
