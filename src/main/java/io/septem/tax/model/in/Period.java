package io.septem.tax.model.in;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Period {

    private final LocalDate dateFrom;
    private final LocalDate dateTo;

}
