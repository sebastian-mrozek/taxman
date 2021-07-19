package io.septem.tax.web;

import io.avaje.http.api.Controller;
import io.avaje.http.api.Get;
import io.avaje.http.api.Path;
import io.septem.tax.logic.TaxReturnService;
import io.septem.tax.model.in.*;
import io.septem.tax.model.out.GstReturn;
import io.septem.tax.model.out.TaxReturn;
import io.septem.tax.persistence.StorageService;
import jakarta.inject.Inject;

import java.util.List;

@Controller
@Path("api/tax-year")
public class TaxYearController {

    private final StorageService storageService;
    private final TaxReturnService taxReturnService;

    @Inject
    public TaxYearController(StorageService storageService, TaxReturnService taxReturnService) {
        this.storageService = storageService;
        this.taxReturnService = taxReturnService;
    }

    @Get("{label}")
    public TaxYear getTaxYear(String label) {
        return storageService.getTaxYear(label);
    }

    @Get("{label}/setup")
    public TaxYearSetup getSetup(String label) {
        return storageService.getTaxYearSetup(label);
    }

    @Get("{label}/invoices")
    public List<Invoice> listInvoices(String label) {
        return storageService.listInvoices(label);
    }

    @Get("{label}/expenses")
    public List<Expense> listExpenses(String label) {
        return storageService.listExpenses(label);
    }

    @Get("{label}/donations")
    public List<Donation> listDonations(String label) {
        return storageService.listDonations(label);
    }

    @Get("{label}/gst-returns")
    public List<GstReturn> listGstReturns(String label) {
        TaxYear taxYear = this.storageService.getTaxYear(label);
        return taxReturnService.calculateGstReturns(taxYear);
    }

    @Get("{label}/tax-return")
    public TaxReturn getTaxReturn(String label) {
        TaxYear taxYear = this.storageService.getTaxYear(label);
        return taxReturnService.calculateTaxReturn(taxYear);
    }

}
