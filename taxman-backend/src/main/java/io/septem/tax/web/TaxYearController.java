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

    @Get("{year}")
    public TaxYear getTaxYear(int year) {
        return storageService.getTaxYear(year);
    }

    @Get("{year}/setup")
    public TaxYearSetup getSetup(int year) {
        return storageService.getTaxYearSetup(year);
    }

    @Get("{year}/invoices")
    public List<Invoice> listInvoices(int year) {
        return storageService.listInvoices(year);
    }

    @Get("{year}/expenses")
    public List<Expense> listExpenses(int year) {
        return storageService.listExpenses(year);
    }

    @Get("{year}/donations")
    public List<Donation> listDonations(int year) {
        return storageService.listDonations(year);
    }

    @Get("{year}/gst-returns")
    public List<GstReturn> listGstReturns(int year) {
        TaxYear taxYear = this.storageService.getTaxYear(year);
        return taxReturnService.calculateGstReturns(taxYear);
    }

    @Get("{year}/tax-return")
    public TaxReturn getTaxReturn(int year) {
        TaxYear taxYear = this.storageService.getTaxYear(year);
        return taxReturnService.calculateTaxReturn(taxYear);
    }

}
