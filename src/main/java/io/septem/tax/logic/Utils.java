package io.septem.tax.logic;

import io.septem.tax.model.in.Period;

import java.time.LocalDate;

public class Utils {

    public static boolean isWithin(LocalDate date, Period period) {
        return date.isAfter(period.getDateFrom().minusDays(1))
                && date.isBefore(period.getDateTo().plusDays(1));
    }
    public static boolean isWithin(Period periodWithin, Period period) {
        return isWithin(periodWithin.getDateFrom(), period) &&
                isWithin(periodWithin.getDateTo(), period);
    }


}
