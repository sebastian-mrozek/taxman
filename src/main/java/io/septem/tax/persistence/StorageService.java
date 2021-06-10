package io.septem.tax.persistence;

import io.septem.tax.model.in.*;

import java.util.List;

public interface StorageService {

    List<String> listTaxYears() throws DataAccessException;

    TaxYear getTaxYear(String label) throws DataAccessException;

    TaxYearSetup getTaxYearSetup(String label) throws DataAccessException;

    List<Invoice> listInvoices(String label) throws DataAccessException;

    List<Expense> listExpenses(String label) throws DataAccessException;

    List<Donation> listDonations(String label) throws DataAccessException;
}
