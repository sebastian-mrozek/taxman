package io.septem.tax.persistence;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import io.septem.tax.mapper.Mapper;
import io.septem.tax.model.in.*;
import io.septem.tax.persistence.csv.model.CsvDonation;
import io.septem.tax.persistence.csv.model.CsvExpense;
import io.septem.tax.persistence.csv.model.CsvInvoice;
import io.septem.tax.web.ServiceFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileStorageService implements StorageService {

    private static final String EXTENSION_JSON = "json";
    private static final String EXTENSION_CSV = "csv";
    private static final String SETUP_SUFFIX = "setup";
    private static final String INVOICES_SUFFIX = "invoices";
    private static final String EXPENSES_SUFFIX = "expenses";
    private static final String DONATIONS_SUFFIX = "donations";

    private final Path folder;

    private final ObjectMapper objectMapper;
    private final CsvMapper csvObjectMapper;
    private final Mapper mapper;

    public FileStorageService(ServiceFactory factory, Path folder) {
        this.folder = folder;

        this.objectMapper = factory.newObjectMapper();
        this.csvObjectMapper = factory.newCsvObjectMapper();
        this.mapper = factory.newMapper();
    }

    @Override
    public List<String> listTaxYears() {
        return new ArrayList<>();
    }

    @Override
    public TaxYear getTaxYear(String label) throws DataAccessException {
        TaxYearSetup setup = getTaxYearSetup(label);
        List<Invoice> invoices = listInvoices(label);
        List<Expense> expenses = listExpenses(label);
        List<Donation> donations = listDonations(label);
        return new TaxYear(setup, invoices, expenses, donations);
    }

    @Override
    public TaxYearSetup getTaxYearSetup(String label) throws DataAccessException {
        try {
            return readObjectJson(label, SETUP_SUFFIX, TaxYearSetup.class);
        } catch (IOException e) {
            throw new DataAccessException("Error reading " + label + " tax year", e);
        }
    }

    @Override
    public List<Invoice> listInvoices(String label) throws DataAccessException {
        try {
            List<CsvInvoice> csvInvoices = readListCsv(label, INVOICES_SUFFIX, CsvInvoice.class);
            return csvInvoices.stream()
                    .map(mapper::invoiceFromCsv)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new DataAccessException("Error reading list of invoices for " + label + " tax year", e);
        }
    }

    @Override
    public List<Expense> listExpenses(String label) throws DataAccessException {
        try {
            List<CsvExpense> csvExpenses = readListCsv(label, EXPENSES_SUFFIX, CsvExpense.class);
            return csvExpenses.stream()
                    .map(mapper::expenseFromCsv)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new DataAccessException("Error reading list of expenses for " + label + " tax year", e);
        }
    }

    @Override
    public List<Donation> listDonations(String label) throws DataAccessException {
        try {
            List<CsvDonation> csvDonations = readListCsv(label, DONATIONS_SUFFIX, CsvDonation.class);
            return csvDonations.stream()
                    .map(mapper::donationFromCsv)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new DataAccessException("Error reading list of donations for " + label + " tax year", e);
        }
    }

    private String getFileContent(String label, String suffix, String extension) throws IOException {
        var fileName = String.format("%s-%s.%s", label, suffix, extension);
        var path = Path.of(folder.toString(), fileName);
        return Files.readString(path);
    }

    private <T> T readObjectJson(String label, String suffix, Class<T> clazz) throws IOException {
        String fileContent = getFileContent(label, suffix, EXTENSION_JSON);
        return this.objectMapper.readValue(fileContent, clazz);
    }

    private <T> List<T> readListJson(String label, String suffix, Class<T> clazz) throws IOException {
        String fileContent = getFileContent(label, suffix, EXTENSION_JSON);
        ObjectReader listReader = objectMapper.readerForListOf(clazz);
        return listReader.readValue(fileContent);
    }

    private <T> List<T> readListCsv(String label, String suffix, Class<T> clazz) throws IOException {
        String fileContent = getFileContent(label, suffix, EXTENSION_CSV);
        CsvSchema columns = csvObjectMapper.schemaFor(clazz).withHeader().withComments();
        MappingIterator<T> iterator = csvObjectMapper
                .readerWithTypedSchemaFor(clazz)
                .with(columns)
                .readValues(fileContent);
        return iterator.readAll();
    }
}
