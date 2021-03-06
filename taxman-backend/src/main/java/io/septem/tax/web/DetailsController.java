package io.septem.tax.web;

import io.avaje.http.api.Controller;
import io.avaje.http.api.Get;
import io.avaje.http.api.Path;
import io.septem.tax.model.input.Donation;
import io.septem.tax.model.input.Expense;
import io.septem.tax.model.input.Invoice;
import io.septem.tax.persistence.StorageService;
import jakarta.inject.Inject;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Path("api")
public class DetailsController {

    private final StorageService storageService;

    @Inject
    public DetailsController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Get("invoices")
    public List<Invoice> listInvoices() {
        return storageService.listInvoices();
    }

    @Get("expenses")
    public List<Expense> listExpenses() {
        return storageService.listExpenses();
    }

    @Get("donations")
    public List<Donation> listDonations() {
        return storageService.listDonations();
    }
}
