package io.septem.tax.mapper;

import io.septem.tax.model.input.*;
import io.septem.tax.persistence.csv.model.CsvDonation;
import io.septem.tax.persistence.csv.model.CsvExpense;
import io.septem.tax.persistence.csv.model.CsvInvoice;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class ModelMapper {

    public Invoice invoiceFromCsv(CsvInvoice csvInvoice) {
        return Invoice.builder()
                .particulars(csvInvoice.getParticulars())
                .customer(csvInvoice.getCustomer())
                .dateIssued(csvInvoice.getDateIssued())
                .datePaid(csvInvoice.getDatePaid())
                .gstPercent(csvInvoice.getGstPercent())
                .withholdingTaxPercent(csvInvoice.getWithholdingTaxPercent())
                .netValue(csvInvoice.getNetValue())
                .build();
    }

    public CsvInvoice invoiceToCsv(Invoice invoice) {
        return new CsvInvoice(
                invoice.getCustomer(),
                invoice.getDateIssued(),
                invoice.getNetValue(),
                invoice.getParticulars(),
                invoice.getGstPercent(),
                invoice.getWithholdingTaxPercent(),
                invoice.getDatePaid());
    }

    public Expense expenseFromCsv(CsvExpense csvExpense) {
        TaxDetail taxDetail1 = createTaxDetail(csvExpense.getTaxableAmount1(), csvExpense.getTaxPercent1());
        TaxDetail taxDetail2 = createTaxDetail(csvExpense.getTaxableAmount2(), csvExpense.getTaxPercent2());

        Expense.ExpenseBuilder expenseBuilder = Expense.builder()
                .expenseTypeName(csvExpense.getExpenseTypeName())
                .invoiceNumber(csvExpense.getInvoiceNumber())
                .particulars(csvExpense.getParticulars())
                .period(createPeriod(csvExpense.getDateFrom(), csvExpense.getDateTo()))
                .description(csvExpense.getDescription());

        List<TaxDetail> gstDetails = Stream.of(taxDetail1, taxDetail2).filter(Objects::nonNull).collect(Collectors.toList());
        expenseBuilder.gstDetails(gstDetails);

        return expenseBuilder.build();
    }

    public CsvExpense expenseToCsv(Expense expense) {
        BigDecimal taxableAmount1 = null, taxPercent1 = null, taxableAmount2 = null, taxPercent2 = null;
        List<TaxDetail> gstDetails = expense.getGstDetails();

        if (gstDetails.size() > 2) {
            throw new IllegalArgumentException("More than 2 gst components are not supported");
        }

        if (gstDetails.size() > 0) {
            taxableAmount1 = gstDetails.get(0).getTaxableAmount();
            taxPercent1 = gstDetails.get(0).getTaxPercent();
        }

        if (gstDetails.size() > 1) {
            taxableAmount2 = gstDetails.get(1).getTaxableAmount();
            taxPercent2 = gstDetails.get(1).getTaxPercent();
        }

        return new CsvExpense(
                expense.getParticulars(),
                expense.getExpenseTypeName(),
                expense.getInvoiceNumber(),
                expense.getPeriod().getDateFrom(),
                expense.getPeriod().getDateTo(),
                taxableAmount1,
                taxPercent1,
                taxableAmount2,
                taxPercent2,
                expense.getDescription());
    }

    public CsvDonation donationToCsv(Donation donation) {
        return new CsvDonation(
                donation.getOrganisation(),
                donation.getRegistrationNo(),
                donation.getPeriod().getDateFrom(),
                donation.getPeriod().getDateTo(),
                donation.getValue());
    }

    public Donation donationFromCsv(CsvDonation csvDonation) {
        return new Donation(
                csvDonation.getOrganisation(),
                csvDonation.getRegistrationNo(),
                new Period(csvDonation.getDateFrom(), csvDonation.getDateTo()),
                csvDonation.getValue());
    }

    private Period createPeriod(LocalDate dateFrom, LocalDate dateTo) {
        return new Period(dateFrom, dateTo);
    }

    private TaxDetail createTaxDetail(BigDecimal taxableAmount, BigDecimal taxPercent) {
        if (taxableAmount != null) {
            return new TaxDetail(taxableAmount, taxPercent);
        }
        return null;
    }
}
