package io.septem.tax.web;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import io.septem.tax.mapper.ModelMapper;
import io.septem.tax.model.input.TaxYear;
import io.septem.tax.persistence.StorageService;
import io.septem.tax.persistence.csv.model.CsvDonation;
import io.septem.tax.persistence.csv.model.CsvExpense;
import io.septem.tax.persistence.csv.model.CsvInvoice;
import jakarta.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CsvConverterApp {

    @Inject
    StorageService storageService;

    @Inject
    ModelMapper modelMapper;

    @Inject
    CsvMapper csvMapper;

    public static void main(String[] args) throws Exception {
        new CsvConverterApp().convert(2019, 2020, 2021);
    }

    public void convert(int... years) throws IOException {
        for (int year : years) {
            convertExpensesToCsv(year);
            convertInvoicesToCsv(year);
            convertDonationsToCsv(year);
        }
    }

    private void convertInvoicesToCsv(int year) throws IOException {
        TaxYear taxYear = storageService.getTaxYear(year);

        List<CsvInvoice> csvInvoices = taxYear.getInvoices().stream()
                .map(modelMapper::invoiceToCsv)
                .collect(Collectors.toList());

        writeAsCsv(CsvInvoice.class, csvInvoices, new File("private/" + year + "-invoices.csv"));

    }

    private void convertExpensesToCsv(int year) throws IOException {
        TaxYear taxYear = storageService.getTaxYear(year);

        List<CsvExpense> csvExpenses = taxYear.getExpenses().stream()
                .map(modelMapper::expenseToCsv)
                .collect(Collectors.toList());

        writeAsCsv(CsvExpense.class, csvExpenses, new File("private/" + year + "-expenses.csv"));

    }

    private void convertDonationsToCsv(int year) throws IOException {
        TaxYear taxYear = storageService.getTaxYear(year);

        List<CsvDonation> csvDonations = taxYear.getDonations().stream()
                .map(modelMapper::donationToCsv)
                .collect(Collectors.toList());

        writeAsCsv(CsvDonation.class, csvDonations, new File("private/" + year + "-donations.csv"));
    }

    private <T> void writeAsCsv(Class<T> clazz, List<T> elements, File file) throws IOException {
        CsvSchema columns = csvMapper.schemaFor(clazz).withHeader();
        ObjectWriter writer = csvMapper.writer(columns);
        writer.writeValue(file, elements);
    }
}
