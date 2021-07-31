package io.septem.tax.model.output;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.septem.tax.model.input.Period;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GstReturn {

    private final Period period;
    private final BigDecimal netIncome;
    private final BigDecimal netExpenses;
    private final BigDecimal gstCollected;
    private final BigDecimal gstPaid;
    private final BigDecimal gstBalance;
    private final BigDecimal profit;
}
