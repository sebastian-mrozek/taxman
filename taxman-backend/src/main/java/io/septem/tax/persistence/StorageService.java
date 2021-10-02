package io.septem.tax.persistence;

import io.septem.tax.model.input.*;

import java.io.IOException;
import java.util.List;

public interface StorageService {

    List<String> listTaxYears() throws DataAccessException;

    TaxYear getTaxYear(int year) throws DataAccessException;

    TaxYearSetup getTaxYearSetup(int year) throws DataAccessException;

    List<Invoice> listInvoices() throws DataAccessException;

    List<Invoice> listInvoices(int year) throws DataAccessException;

    List<Expense> listExpenses() throws DataAccessException;

    List<Expense> listExpenses(int year) throws DataAccessException;

    List<Donation> listDonations() throws DataAccessException;

    List<Donation> listDonations(int year) throws DataAccessException;

    void saveTaxYearSetups(List<TaxYearSetup> taxYearSetups);

    void addInvoice(Invoice invoice);

    void saveInvoices(List<Invoice> invoices);

    void addExpense(Expense expense);

    void saveExpenses(List<Expense> expenses);

    void addDonation(Donation donation);

    void saveDonations(List<Donation> donations);
}
