import type { Column } from "./columns";
import PeriodView from "./PeriodView.svelte";
import TaxView from "./TaxView.svelte";
import currency from "currency.js";

export const EXPENSE_COLS: Column[] = [
  { label: "Particulars", type: { field: "particulars" } },
  { label: "Type", type: { field: "expenseTypeName" } },
  { label: "Number", type: { field: "invoiceNumber" } },
  {
    label: "Period",
    type: {
      component: PeriodView,
      getProps: (expense) => {
        return { period: expense.period };
      },
    },
  },
  { label: "Gross value", type: { getText: (expense) => currency(expense.grossValue).format() } },
  {
    label: "GST",
    type: {
      component: TaxView,
      getProps: (invoice) => invoice.gstDetail[0],
    },
  },
];
