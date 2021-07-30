package io.septem.tax.web;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import io.septem.tax.mapper.Mapper;
import io.septem.tax.model.in.TaxYear;
import io.septem.tax.persistence.csv.model.CsvDonation;
import io.septem.tax.persistence.csv.model.CsvExpense;
import io.septem.tax.persistence.csv.model.CsvInvoice;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CsvConverterApp {

    private final ServiceFactory factory = new ServiceFactory();

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
        TaxYear taxYear = factory.newStorageService().getTaxYear(year);

        Mapper mapper = factory.newMapper();
        List<CsvInvoice> csvInvoices = taxYear.getInvoices().stream()
                .map(mapper::invoiceToCsv)
                .collect(Collectors.toList());

        writeAsCsv(CsvInvoice.class, csvInvoices, new File("private/" + year + "-invoices.csv"));

    }

    private void convertExpensesToCsv(int year) throws IOException {
        TaxYear taxYear = factory.newStorageService().getTaxYear(year);

        Mapper mapper = factory.newMapper();
        List<CsvExpense> csvExpenses = taxYear.getExpenses().stream()
                .map(mapper::expenseToCsv)
                .collect(Collectors.toList());

        writeAsCsv(CsvExpense.class, csvExpenses, new File("private/" + year + "-expenses.csv"));

    }

    private void convertDonationsToCsv(int year) throws IOException {
        TaxYear taxYear = factory.newStorageService().getTaxYear(year);

        Mapper mapper = factory.newMapper();
        List<CsvDonation> csvDonations = taxYear.getDonations().stream()
                .map(mapper::donationToCsv)
                .collect(Collectors.toList());

        writeAsCsv(CsvDonation.class, csvDonations, new File("private/" + year + "-donations.csv"));
    }

    private <T> void writeAsCsv(Class<T> clazz, List<T> elements, File file) throws IOException {
        CsvMapper csvMapper = factory.newCsvObjectMapper();
        CsvSchema columns = csvMapper.schemaFor(clazz).withHeader();
        ObjectWriter writer = csvMapper.writer(columns);
        writer.writeValue(file, elements);
    }
}
