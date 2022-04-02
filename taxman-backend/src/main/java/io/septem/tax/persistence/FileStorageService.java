package io.septem.tax.persistence;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import io.septem.tax.logic.Utils;
import io.septem.tax.mapper.ModelMapper;
import io.septem.tax.model.input.*;
import io.septem.tax.persistence.csv.model.CsvDonation;
import io.septem.tax.persistence.csv.model.CsvExpense;
import io.septem.tax.persistence.csv.model.CsvInvoice;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
    private static final String[] EXPENSES_COLUMNS_ORDER = new String[]{"invoiceNumber", "particulars", "expenseTypeName", "dateFrom", "dateTo", "taxableAmount1", "taxPercent1", "taxableAmount2", "taxPercent2", "description"};
    private static final String[] DONATIONS_COLUMNS_ORDER = new String[]{"dateFrom", "dateTo", "organisation", "registrationNo", "value"};
    private static final String[] INVOICES_COLUMNS_ORDER = new String[]{"customer", "dateIssued", "netValue", "particulars", "gstPercent", "withholdingTaxPercent", "datePaid"};

    private final Path folder;

    private final ObjectMapper jsonMapper;
    private final CsvMapper csvMapper;
    private final ModelMapper modelMapper;

    @Inject
    public FileStorageService(ObjectMapper jsonMapper, CsvMapper csvMapper, ModelMapper modelMapper, Path dataFolder) {
        this.folder = dataFolder;
        this.csvMapper = csvMapper;
        this.jsonMapper = jsonMapper;
        this.modelMapper = modelMapper;
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

    private <T> Predicate<T> isForTaxYear(int year, Function<T, LocalDate> dateExtractor) {
        Period taxYearPeriod = Utils.taxYearPeriod(year);
        return element -> Utils.isWithin(dateExtractor.apply(element), taxYearPeriod);
    }

    public List<Invoice> listInvoices() throws DataAccessException {
        try {
            List<CsvInvoice> csvInvoices = readListCsv(fileName(INVOICES_SUFFIX, EXTENSION_CSV), CsvInvoice.class);
            return csvInvoices.stream()
                    .map(modelMapper::invoiceFromCsv)
                    .sorted(Comparator.comparing(Invoice::getDateIssued))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.info("Error reading invoices: {}", e.getMessage(), e);
            throw new DataAccessException("Error reading list of invoices", e);
        }
    }

    public List<Invoice> listInvoices(int year) throws DataAccessException {
        return listInvoices().stream()
                .filter(isForTaxYear(year, Invoice::getDatePaid))
                .sorted(Comparator.comparing(Invoice::getDateIssued))
                .collect(Collectors.toList());
    }

    public List<Expense> listExpenses() throws DataAccessException {
        try {
            List<CsvExpense> csvExpenses = readListCsv(fileName(EXPENSES_SUFFIX, EXTENSION_CSV), CsvExpense.class);
            return csvExpenses.stream()
                    .map(modelMapper::expenseFromCsv)
                    .sorted(Comparator.comparing(Expense::getPeriod))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new DataAccessException("Error reading list of expenses", e);
        }
    }

    @Override
    public List<Expense> listExpenses(int year) throws DataAccessException {
        return listExpenses().stream()
                .filter(isForTaxYear(year, expense -> expense.getPeriod().getDateFrom()))
                .sorted(Comparator.comparing(Expense::getPeriod))
                .collect(Collectors.toList());
    }

    public List<Donation> listDonations() throws DataAccessException {
        try {
            List<CsvDonation> csvDonations = readListCsv(fileName(DONATIONS_SUFFIX, EXTENSION_CSV), CsvDonation.class);
            return csvDonations.stream()
                    .map(modelMapper::donationFromCsv)
                    .sorted(Comparator.comparing(Donation::getPeriod))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new DataAccessException("Error reading list of donations", e);
        }
    }

    @Override
    public List<Donation> listDonations(int year) throws DataAccessException {
        return listDonations().stream()
                .filter(isForTaxYear(year, donation -> donation.getPeriod().getDateFrom()))
                .sorted(Comparator.comparing(Donation::getPeriod))
                .collect(Collectors.toList());
    }

    @Override
    public void addInvoice(Invoice invoice) {
        List<Invoice> invoices = listInvoices();
        invoices.add(invoice);
        saveInvoices(invoices);

    }

    @Override
    public void saveInvoices(List<Invoice> invoices) {
        mapToCsvAndSave(INVOICES_SUFFIX, invoices, this.modelMapper::invoiceToCsv, CsvInvoice.class, INVOICES_COLUMNS_ORDER);
    }


    @Override
    public void addExpense(Expense expense) {
        List<Expense> expenses = listExpenses();
        expenses.add(expense);
        saveExpenses(expenses);
    }

    @Override
    public void saveExpenses(List<Expense> expenses) {
        mapToCsvAndSave(EXPENSES_SUFFIX, expenses, this.modelMapper::expenseToCsv, CsvExpense.class, EXPENSES_COLUMNS_ORDER);
    }

    private <T, R> void mapToCsvAndSave(String suffix, List<T> items, Function<T, R> mapper, Class<R> csvClass, String... columnOrder) {
        String fileName = fileName(suffix, EXTENSION_CSV);
        List<R> csvExpenses = items.stream()
                .map(mapper)
                .collect(Collectors.toList());
        try {
            writeAsCsv(csvClass, csvExpenses, fileName, columnOrder);
        } catch (IOException e) {
            throw new DataAccessException("Error writing data to " + fileName, e);
        }
    }

    @Override
    public void addDonation(Donation donation) {
        List<Donation> donations = listDonations();
        donations.add(donation);
        saveDonations(donations);

    }

    @Override
    public void saveDonations(List<Donation> donations) {
        mapToCsvAndSave(DONATIONS_SUFFIX, donations, this.modelMapper::donationToCsv, CsvDonation.class, DONATIONS_COLUMNS_ORDER);
    }

    private String fileName(String name, String extension) {
        return String.format("%s.%s", name, extension);
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
        return this.jsonMapper.readValue(fileContent, clazz);
    }

    private <T> List<T> readListJson(String label, String suffix, Class<T> clazz) throws IOException {
        String fileContent = getFileContent(label, suffix, EXTENSION_JSON);
        ObjectReader listReader = jsonMapper.readerForListOf(clazz);
        return listReader.readValue(fileContent);
    }

    private <T> List<T> readListCsv(String fileName, Class<T> clazz) throws IOException {
        String fileContent = getFileContent(fileName);
        CsvSchema columns = csvMapper.schemaFor(clazz).withUseHeader(true).withComments().withHeader().withColumnReordering(true);
        MappingIterator<T> iterator = csvMapper
                .readerWithSchemaFor(clazz)
                .with(columns)
                .readValues(fileContent);
        return iterator.readAll();
    }

    private <T> void writeAsCsv(Class<T> clazz, List<T> elements, String fileName, String... columnsOrder) throws IOException {
        CsvSchema columns = csvMapper.schemaFor(clazz).withHeader().sortedBy(columnsOrder);
        ObjectWriter writer = csvMapper.writer(columns);
        Path path = Path.of(folder.toString(), fileName);
        writer.writeValue(path.toFile(), elements);
    }

    @Override
    public void saveTaxYearSetups(List<TaxYearSetup> taxYearSetups) {
        for (TaxYearSetup taxYearSetup : taxYearSetups) {
            try {
                saveTaxYearSetup(taxYearSetup);
            } catch (IOException e) {
                throw new DataAccessException("Failed to save tax year setup: " + taxYearSetup.getLabel(), e);
            }
        }
    }

    private void saveTaxYearSetup(TaxYearSetup taxYearSetup) throws IOException {
        var fileName = String.format("%s-%s.%s", taxYearSetup.getLabel(), SETUP_SUFFIX, EXTENSION_JSON);
        Path path = Path.of(folder.toString(), fileName);
        jsonMapper.writeValue(path.toFile(), taxYearSetup);
    }
}
