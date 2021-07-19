package io.septem.tax.persistence.csv.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CsvDonation {

    private final String organisation;
    private final String registrationNo;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final BigDecimal value;
}
