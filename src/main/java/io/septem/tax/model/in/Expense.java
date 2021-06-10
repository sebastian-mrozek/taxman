package io.septem.tax.model.in;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Expense {

    private final String particulars;
    private final String expenseTypeName;
    private final String invoiceNumber;
    private final Period period;
    private final BigDecimal grossValue;
    private final GstDetail gstDetail;
}
