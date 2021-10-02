package io.septem.tax.sampledata;

import io.septem.tax.model.input.*;
import io.septem.tax.persistence.StorageService;
import io.septem.tax.web.ServiceFactory;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SampleDataGenerator {

    private static final BigDecimal GST_PERCENT = BigDecimal.valueOf(15);
    private static final BigDecimal WITHHOLDING_PERCENT = BigDecimal.TEN;
    final StorageService storageService;

    public SampleDataGenerator(StorageService storageService) {
        this.storageService = storageService;
    }

    public static void main(String[] args) {
        ServiceFactory factory = new ServiceFactory();
        StorageService storageService = factory.fileStorageService();

        new SampleDataGenerator(storageService).generate(2019, 2020, 2021, 2022);
    }

    private void generate(Integer... taxYears) {
        List<ExpenseType> expenseTypes = generateExpenseTypes();
        List<TaxYearSetup> taxYearSetups = generateTaxYearSetups(taxYears, expenseTypes);
        List<Invoice> invoices = generateInvoices(taxYears);
        List<Expense> expenses = generateExpenses(expenseTypes, taxYears);
        List<Donation> donations = generateDonations(taxYears);

        storageService.saveTaxYearSetups(taxYearSetups);
        storageService.saveInvoices(invoices);
        storageService.saveExpenses(expenses);
        storageService.saveDonations(donations);
    }

    private List<TaxYearSetup> generateTaxYearSetups(Integer[] taxYears, List<ExpenseType> expenseTypes) {
        return Arrays.stream(taxYears).map(year -> new TaxYearSetup(year.toString(),
                generateGstReturnPeriods(year),
                expenseTypes,
                generateIncomeTaxRates())).collect(Collectors.toList());
    }

    @NotNull
    private List<Period> generateGstReturnPeriods(Integer year) {
        LocalDate apr1st = LocalDate.of(year - 1, 4, 1);
        LocalDate sep30th = LocalDate.of(year - 1, 9, 30);
        Period period1 = new Period(apr1st, sep30th);
        LocalDate oct1st = LocalDate.of(year - 1, 10, 1);
        LocalDate march31st = LocalDate.of(year, 3, 31);
        Period period2 = new Period(oct1st, march31st);
        return List.of(period1, period2);
    }

    private List<IncomeTaxRate> generateIncomeTaxRates() {
        return List.of(new IncomeTaxRate(BigDecimal.valueOf(0), BigDecimal.valueOf(10.5)),
                new IncomeTaxRate(BigDecimal.valueOf(14000), BigDecimal.valueOf(17.5)),
                new IncomeTaxRate(BigDecimal.valueOf(48000), BigDecimal.valueOf(30.0)),
                new IncomeTaxRate(BigDecimal.valueOf(70000), BigDecimal.valueOf(33.0)),
                new IncomeTaxRate(BigDecimal.valueOf(180000), BigDecimal.valueOf(39.0)));
    }

    private List<ExpenseType> generateExpenseTypes() {
        return List.of(new ExpenseType("ITEquipment", BigDecimal.valueOf(100.0)),
                new ExpenseType("ProfessionalMembership", BigDecimal.valueOf(100.0)),
                new ExpenseType("BankFees", BigDecimal.valueOf(100.0)),
                new ExpenseType("MobilePhone", BigDecimal.valueOf(50.0)),
                new ExpenseType("Broadband", BigDecimal.valueOf(50.0)),
                new ExpenseType("Electricity", BigDecimal.valueOf(9.0)),
                new ExpenseType("MortgageInterest", BigDecimal.valueOf(9.0)),
                new ExpenseType("HouseInsurance", BigDecimal.valueOf(9.0)),
                new ExpenseType("Rates", BigDecimal.valueOf(9.0)));
    }

    private List<Invoice> generateInvoices(Integer[] taxYears) {
        return Arrays.stream(taxYears)
                .flatMap(year -> randomInvoices(year).stream())
                .collect(Collectors.toList());
    }

    private List<Invoice> randomInvoices(Integer year) {
        return randomRange(10).mapToObj((i) -> randomInvoice(i, year)).collect(Collectors.toList());
    }

    private List<Expense> randomExpenses(List<ExpenseType> expenseTypes, Integer year) {
        return randomRange(5).mapToObj((i) -> randomExpense(expenseTypes, i, year)).collect(Collectors.toList());
    }

    private List<Donation> randomDonations(Integer year) {
        return randomRange(3).mapToObj((i) -> randomDonation(i, year)).collect(Collectors.toList());
    }

    private Donation randomDonation(int i, Integer year) {
        LocalDate date = randomDateInYear(year);
        return new Donation("org " + i, "reg " + i, new Period(date, date), randomValue(100));
    }

    private Invoice randomInvoice(Integer count, Integer year) {
        LocalDate date = randomDateInYear(year);
        return Invoice.builder()
                .particulars("something" + count)
                .customer("customer")
                .dateIssued(date)
                .datePaid(date.plusDays(1))
                .netValue(randomValue(5000))
                .gstPercent(GST_PERCENT)
                .withholdingTaxPercent(WITHHOLDING_PERCENT)
                .build();
    }

    private Expense randomExpense(List<ExpenseType> expenseTypes, Integer count, Integer year) {
        LocalDate date = randomDateInYear(year);
        return Expense.builder()
                .particulars("expense" + count)
                .description("stuff I need " + count)
                .invoiceNumber("invoice " + count)
                .expenseTypeName(randomExpenseType(expenseTypes).getName())
                .gstDetails(List.of(new TaxDetail(randomValue(500), GST_PERCENT)))
                .period(new Period(date, date))
                .build();
    }

    private ExpenseType randomExpenseType(List<ExpenseType> expenseTypes) {
        int n = new Random().nextInt(expenseTypes.size());
        return expenseTypes.get(n);
    }

    private BigDecimal randomValue(int min) {
        return BigDecimal.valueOf(min + new Random().nextDouble() * min).setScale(2, RoundingMode.HALF_UP);
    }

    private LocalDate randomDateInYear(Integer taxYear) {
        int calYear = taxYear - new Random().nextInt(2);
        int month;
        if (calYear == taxYear) {
            month = 1 + new Random().nextInt(3);
        } else {
            month = 4 + new Random().nextInt(9);
        }
        int day = 5 + new Random().nextInt(20);
        return LocalDate.of(calYear, month, day);
    }

    private IntStream randomRange(int min) {
        int n = new Random().nextInt(min);
        return IntStream.range(1, min + n);
    }

    private List<Expense> generateExpenses(List<ExpenseType> expenseTypes, Integer[] taxYears) {
        return Arrays.stream(taxYears)
                .flatMap(year -> randomExpenses(expenseTypes, year).stream())
                .collect(Collectors.toList());
    }

    private List<Donation> generateDonations(Integer[] taxYears) {
        return Arrays.stream(taxYears)
                .flatMap(year -> randomDonations(year).stream())
                .collect(Collectors.toList());
    }
}
