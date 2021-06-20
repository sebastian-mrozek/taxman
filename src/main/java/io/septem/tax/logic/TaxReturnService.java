package io.septem.tax.logic;

import io.septem.tax.model.in.*;
import io.septem.tax.model.out.GstReturn;
import io.septem.tax.model.out.TaxReturn;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaxReturnService {

    public TaxReturn calculateTaxReturn(TaxYear taxYear) {
        return TaxReturn.builder()
                .year(taxYear.getSetup().getLabel())
                .gstReturns(calculateGstReturns(taxYear))
                .netIncome(sumNetIncome(taxYear.getInvoices()))
                .netExpenses(sumNetExpenses(taxYear.getExpenses()))
                .build();
    }

    public List<GstReturn> calculateGstReturns(TaxYear taxYear) {
        List<GstReturn> gstReturns = new LinkedList<>();

        List<Period> gstReturnPeriods = taxYear.getSetup().getGstReturnPeriods();
        for (Period gstReturnPeriod : gstReturnPeriods) {
            List<Invoice> invoices = filterInvoices(taxYear.getInvoices(), gstReturnPeriod);
            List<Expense> expenses = filterExpenses(taxYear.getExpenses(), gstReturnPeriod);
            GstReturn gstReturn = calculateGstReturn(gstReturnPeriod, invoices, expenses);
            gstReturns.add(gstReturn);
        }

        return gstReturns;
    }

    private GstReturn calculateGstReturn(Period gstReturnPeriod, List<Invoice> invoices, List<Expense> expenses) {
        return GstReturn.builder()
                .period(gstReturnPeriod)
                .gstCollected(sumGst(invoices, this::getTaxValue))
                .gstPaid(sumGst(expenses, this::aggregateExpenseTaxValues))
                .netIncome(sumNetIncome(invoices))
                .netExpenses(sumNetExpenses(expenses))
                .build();
    }

    private BigDecimal getTaxValue(Invoice invoice) {
        return invoice.getGstDetail().getTaxValue();
    }

    private BigDecimal aggregateExpenseTaxValues(Expense expense) {
        return expense.getGstDetail().stream()
                .map(TaxDetail::getTaxValue)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private <T> BigDecimal sumGst(List<T> elements, Function<T, BigDecimal> gstGetter) {
        return elements.stream()
                .map(gstGetter)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal sumNetExpenses(List<Expense> expenses) {
        return expenses.stream()
                .map(expense -> expense.getGrossValue().subtract(aggregateExpenseTaxValues(expense)))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal sumNetIncome(List<Invoice> invoices) {
        return invoices.stream()
                .map(Invoice::getNetValue)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private List<Expense> filterExpenses(List<Expense> expenses, Period gstReturnPeriod) {
        return expenses.stream()
                .filter(expense -> Utils.isWithin(expense.getPeriod(), gstReturnPeriod))
                .collect(Collectors.toList());
    }

    private List<Invoice> filterInvoices(List<Invoice> invoices, Period gstReturnPeriod) {
        return invoices.stream()
                .filter(invoice -> Utils.isWithin(invoice.getDateIssued(), gstReturnPeriod))
                .collect(Collectors.toList());
    }
}
