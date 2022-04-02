package io.septem.tax.model.input;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Comparator;

@Data
public class Period implements Comparable<Period> {

    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    @Override
    public int compareTo(@NotNull Period o) {
        return Comparator.comparing(Period::getDateFrom).compare(this, o);
    }
}
