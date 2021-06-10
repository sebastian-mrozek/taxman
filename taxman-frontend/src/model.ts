export type Period = {
  dateFrom: Date;
  dateTo: Date;
};

export type ExpenseType = {
  name: string;
  percentageDeductible: number;
};

export type TaxYearSetup = {
  label: string;
  gstReturnPeriods: Period[];
  expenseTypes: ExpenseType[];
};

export type GstDetail = {
  grossTaxableAmount: number;
  taxPercent: number;
};

export type WithholdingTax = {
  taxableAmount: number;
  percentage: number;
};

export type Invoice = {
  particulars: string;
  customer: string;
  dateIssued: Date;
  netValue: number;
  gstDetails: GstDetail;
  withholdingTax: WithholdingTax;
};

export type Expense = {
  particulars: string;
  expenseTypeName: string;
  invoiceNumber: string;
  period: Period;
  grossValue: number;
  gstDetails: GstDetail;
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
