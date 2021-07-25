package io.septem.tax.logic;

import io.septem.tax.model.in.*;
import io.septem.tax.model.out.ExpenseClaim;
import io.septem.tax.model.out.GstReturn;
import io.septem.tax.model.out.TaxReturn;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaxReturnService {

    public TaxReturn calculateTaxReturn(TaxYear taxYear) {
        List<Invoice> invoices = taxYear.getInvoices();
        List<ExpenseClaim> expenseClaims = reconcileExpenseClaims(taxYear.getSetup().getExpenseTypes(), taxYear.getExpenses());
        BigDecimal incomeTaxPaid = calculateIncomeTaxPaid(invoices);
        BigDecimal netIncome = calculateNetIncome(invoices);
        BigDecimal totalExpensesClaim = calculateTotalExpensesClaim(expenseClaims);
        BigDecimal profit = netIncome.subtract(totalExpensesClaim);
        BigDecimal incomeTax = calculateIncomeTax(taxYear.getSetup().getIncomeTaxRates(), profit);
        BigDecimal remainingIncomeTax = incomeTax.subtract(incomeTaxPaid);
        BigDecimal effectiveIncomeTaxPercent = calculateEffectiveIncomeTaxPercent(incomeTax, profit);
        List<GstReturn> gstReturns = calculateGstReturns(taxYear);

        return TaxReturn.builder()
                .year(taxYear.getSetup().getLabel())
                .incomeTax(incomeTax)
                .invoices(invoices)
                .expenseClaims(expenseClaims)
                .gstReturns(gstReturns)
                .profit(profit)
                .totalExpensesClaim(totalExpensesClaim)
                .netIncome(netIncome)
                .incomeTaxPaid(incomeTaxPaid)
                .remainingIncomeTax(remainingIncomeTax)
                .effectiveIncomeTaxPercent(effectiveIncomeTaxPercent)
                .build();
    }

    private BigDecimal calculateIncomeTax(List<IncomeTaxRate> incomeTaxRates, BigDecimal profit) {
        BigDecimal taxable = profit;
        BigDecimal incomeTax = BigDecimal.ZERO;
        incomeTaxRates.sort(Comparator.comparing(IncomeTaxRate::getAbove).reversed());
        for (IncomeTaxRate incomeTaxRate : incomeTaxRates) {
            if (taxable.doubleValue() > incomeTaxRate.getAbove().doubleValue()) {
                BigDecimal taxableAtRate = taxable.subtract(incomeTaxRate.getAbove());
                BigDecimal tax = taxableAtRate.multiply(incomeTaxRate.getRate()).divide(BigDecimal.valueOf(100.0), 3, RoundingMode.HALF_UP);
                incomeTax = incomeTax.add(tax);
                taxable = taxable.subtract(taxableAtRate);
            }
        }
        return incomeTax;
    }

    private List<ExpenseClaim> reconcileExpenseClaims(List<ExpenseType> expenseTypes, List<Expense> expenses) {
        Map<String, BigDecimal> expensePercentageByName = expenseTypes.stream()
                .collect(Collectors.toMap(ExpenseType::getName, ExpenseType::getPercentDeductible));
        return expenses.stream()
                .filter(expense -> expensePercentageByName.containsKey(expense.getExpenseTypeName()))
                .map(expense -> createExpenseClaim(expensePercentageByName.get(expense.getExpenseTypeName()), expense))
                .collect(Collectors.toList());
    }

    private ExpenseClaim createExpenseClaim(BigDecimal claimPercentage, Expense expense) {
        return ExpenseClaim.builder()
                .claimPercent(claimPercentage)
                .claimValue(expense.getNetValue())
                .expense(expense)
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
        BigDecimal gstPaid = sumGst(expenses, this::aggregateExpenseTaxValues);
        BigDecimal gstCollected = sumGst(invoices, this::getTaxValue);
        BigDecimal netIncome = sumNetIncome(invoices);
        BigDecimal netExpenses = sumNetExpenses(expenses);
        BigDecimal gstBalance = gstPaid.subtract(gstCollected);
        BigDecimal profit = netIncome.subtract(netExpenses);

        return GstReturn.builder()
                .period(gstReturnPeriod)
                .gstCollected(gstCollected)
                .gstPaid(gstPaid)
                .netIncome(netIncome)
                .netExpenses(netExpenses)
                .gstBalance(gstBalance)
                .profit(profit)
                .build();
    }

    private BigDecimal getTaxValue(Invoice invoice) {
        return invoice.getGstDetail().getTaxValue();
    }

    private BigDecimal aggregateExpenseTaxValues(Expense expense) {
        return expense.getGstDetails().stream()
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


    public BigDecimal calculateEffectiveIncomeTaxPercent(BigDecimal incomeTax, BigDecimal profit) {
        return incomeTax
                .divide(profit, 3, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100.0));
    }

    public BigDecimal calculateIncomeTaxPaid(List<Invoice> invoices) {
        return invoices.stream()
                .map(Invoice::getWithholdingTaxDetail)
                .filter(Objects::nonNull)
                .map(TaxDetail::getTaxValue)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateNetIncome(List<Invoice> invoices) {
        return invoices.stream()
                .map(Invoice::getNetValue)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateTotalExpensesClaim(List<ExpenseClaim> expenseClaims) {
        return expenseClaims.stream()
                .map(ExpenseClaim::getClaimValue)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }


}
