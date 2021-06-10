package io.septem.tax.persistence;

import io.septem.tax.model.in.Donation;
import io.septem.tax.model.in.Expense;
import io.septem.tax.model.in.Invoice;
import io.septem.tax.model.in.TaxYearSetup;

import java.nio.file.Path;
import java.util.List;

public class TestApp {

    public static void main(String[] args) throws DataAccessException {
        JsonFileStorageService service = new JsonFileStorageService(Path.of("private"));

        for (String yearLabel : new String[]{"2019", "2020", "2021"}) {
            System.out.println(yearLabel);

            TaxYearSetup taxYearSetup = service.getTaxYearSetup(yearLabel);
            System.out.println(taxYearSetup);

            List<Invoice> invoices = service.listInvoices(yearLabel);
            System.out.println(invoices);

            List<Expense> expenses = service.listExpenses(yearLabel);
            System.out.println(expenses);

            List<Donation> donations = service.listDonations(yearLabel);
            System.out.println(donations);

            System.out.println();
        }

    }

}
