package io.septem.tax.persistence;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import io.septem.tax.logic.Utils;
import io.septem.tax.mapper.Mapper;
import io.septem.tax.model.in.*;
import io.septem.tax.persistence.csv.model.CsvDonation;
import io.septem.tax.persistence.csv.model.CsvExpense;
import io.septem.tax.persistence.csv.model.CsvInvoice;
import io.septem.tax.web.ServiceFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
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
    public TaxYear getTaxYear(int year) throws DataAccessException {
        TaxYearSetup setup = getTaxYearSetup(year);
        List<Invoice> invoices = listInvoices(year);
        List<Expense> expenses = listExpenses(year);
        List<Donation> donations = listDonations(year);
        return new TaxYear(setup, invoices, expenses, donations);
    }

    @Override
    public TaxYearSetup getTaxYearSetup(int year) throws DataAccessException {
        try {
            return readObjectJson(Integer.toString(year), SETUP_SUFFIX, TaxYearSetup.class);
        } catch (IOException e) {
            throw new DataAccessException("Error reading " + year + " tax year", e);
        }
    }

    @Override
    public List<Invoice> listInvoices(int year) throws DataAccessException {
        try {
            List<CsvInvoice> csvInvoices = readListCsv(fileName(INVOICES_SUFFIX, EXTENSION_CSV), CsvInvoice.class);
            return csvInvoices.stream()
                    .map(mapper::invoiceFromCsv)
                    .filter(isForTaxYear(year, Invoice::getDatePaid))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.info("Error reading invoices: {}", e.getMessage(), e);
            throw new DataAccessException("Error reading list of invoices for " + year + " tax year", e);
        }
    }

    private <T> Predicate<T> isForTaxYear(int year, Function<T, LocalDate> dateExtractor) {
        Period taxYearPeriod = Utils.taxYearPeriod(year);
        return element -> Utils.isWithin(dateExtractor.apply(element), taxYearPeriod);
    }

    @Override
    public List<Expense> listExpenses(int year) throws DataAccessException {
        try {
            List<CsvExpense> csvExpenses = readListCsv(fileName(EXPENSES_SUFFIX, EXTENSION_CSV), CsvExpense.class);
            return csvExpenses.stream()
                    .map(mapper::expenseFromCsv)
                    .filter(isForTaxYear(year, expense -> expense.getPeriod().getDateFrom()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new DataAccessException("Error reading list of expenses for " + year + " tax year", e);
        }
    }

    private String fileName(String name, String extension) {
        return String.format("%s.%s", name, extension);
    }

    @Override
    public List<Donation> listDonations(int year) throws DataAccessException {
        try {
            List<CsvDonation> csvDonations = readListCsv(fileName(DONATIONS_SUFFIX, EXTENSION_CSV), CsvDonation.class);
            return csvDonations.stream()
                    .map(mapper::donationFromCsv)
                    .filter(isForTaxYear(year, donation -> donation.getPeriod().getDateFrom()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new DataAccessException("Error reading list of donations for " + year + " tax year", e);
        }
    }

    private String getFileContent(String label, String suffix, String extension) throws IOException {
        var fileName = String.format("%s-%s.%s", label, suffix, extension);
        var path = Path.of(folder.toString(), fileName);
        return Files.readString(path);
    }

    private String getFileContent(String fileName) throws IOException {
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

    private <T> List<T> readListCsv(String fileName, Class<T> clazz) throws IOException {
        String fileContent = getFileContent(fileName);
        CsvSchema columns = csvObjectMapper.schemaFor(clazz).withUseHeader(true).withComments().withHeader().withColumnReordering(true);
        MappingIterator<T> iterator = csvObjectMapper
                .readerWithSchemaFor(clazz)
                .with(columns)
                .readValues(fileContent);
        return iterator.readAll();
    }
}
