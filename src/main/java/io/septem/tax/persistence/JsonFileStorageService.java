package io.septem.tax.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.septem.tax.model.in.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonFileStorageService implements StorageService {

    private static final String EXTENSION = "json";
    private static final String SETUP_SUFFIX = "setup";
    private static final String INVOICES_SUFFIX = "invoices";
    private static final String EXPENSES_SUFFIX = "expenses";
    private static final String DONATIONS_SUFFIX = "donations";

    private final Path folder;
    private final ObjectMapper objectMapper;

    public JsonFileStorageService(Path folder) {
        this.folder = folder;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
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
            return readObject(label, SETUP_SUFFIX, TaxYearSetup.class);
        } catch (IOException e) {
            throw new DataAccessException("Error reading " + label + " tax year", e);
        }
    }

    @Override
    public List<Invoice> listInvoices(String label) throws DataAccessException {
        try {
            return readList(label, INVOICES_SUFFIX, Invoice.class);
        } catch (IOException e) {
            throw new DataAccessException("Error reading list of invoices for " + label + " tax year", e);
        }
    }

    @Override
    public List<Expense> listExpenses(String label) throws DataAccessException {
        try {
            return readList(label, EXPENSES_SUFFIX, Expense.class);
        } catch (IOException e) {
            throw new DataAccessException("Error reading list of expenses for " + label + " tax year", e);
        }
    }

    @Override
    public List<Donation> listDonations(String label) throws DataAccessException {
        try {
            return readList(label, DONATIONS_SUFFIX, Donation.class);
        } catch (IOException e) {
            throw new DataAccessException("Error reading list of donations for " + label + " tax year", e);
        }
    }

    private String getFileContent(String label, String suffix) throws IOException {
        var fileName = String.format("%s-%s.%s", label, suffix, EXTENSION);
        var path = Path.of(folder.toString(), fileName);
        return Files.readString(path);
    }

    private <T> T readObject(String label, String suffix, Class<T> clazz) throws IOException {
        String fileContent = getFileContent(label, suffix);
        return this.objectMapper.readValue(fileContent, clazz);
    }

    private <T> List<T> readList(String label, String suffix, Class<T> clazz) throws IOException {
        String fileContent = getFileContent(label, suffix);
        ObjectReader listReader = objectMapper.readerForListOf(clazz);
        return listReader.readValue(fileContent);
    }
}
