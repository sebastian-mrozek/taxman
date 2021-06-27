export type Period = {
  dateFrom: Date;
  dateTo: Date;
};

export type ExpenseType = {
  name: string;
  percentDeductible: number;
};

export type TaxYearSetup = {
  label: string;
  gstReturnPeriods: Period[];
  expenseTypes: ExpenseType[];
};

export type TaxDetail = {
  taxableAmount: number;
  taxPercent: number;
  taxValue: number;
};

export type Invoice = {
  particulars: string;
  customer: string;
  dateIssued: Date;
  netValue: number;
  gstDetail: TaxDetail;
  withholdingTaxPercent: number;
  withholdingTaxDetail: TaxDetail;
  toPay: number;
};

export type Expense = {
  particulars: string;
  expenseTypeName: string;
  invoiceNumber: string;
  period: Period;
  grossValue: number;
  gstDetails: TaxDetail[];
};

export type Donation = {
  organisation: string;
  registrationNo: string;
  period: Period;
  value: number;
};

export type TaxYear = {
  setup: TaxYearSetup;
  invoices: Invoice[];
  expenses: Expense[];
  donations: Donation[];
};

export type GstReturn = {
  period: Period;
  netIncome: number;
  netExpenses: number;
  profit: number;
  gstCollected: number;
  gstPaid: number;
  gstBalance: number;
};

export type ExpenseClaim = {
  expense: Expense;
  claimValue: number;
  claimPercent: number;
};

export type TaxReturn = {
  year: string;
  netIncome: number;
  totalExpensesClaim: number;
  profit: number;
  incomeTaxPaid: number;
  gstReturns: GstReturn[];
  invoices: Invoice[];
  expenseClaims: ExpenseClaim[];
  incomeTax: number;
  remainingIncomeTax: number;
  effectiveIncomeTaxPercent: number;
};
