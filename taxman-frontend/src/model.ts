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
  gstDetails: TaxDetail;
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
  gstDetails: TaxDetail;
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
