import type { Column } from "./columns";
import PeriodView from "./PeriodView.svelte";

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
  { label: "Gross value", type: { field: "grossValue" } },
  {
    label: "GST",
    type: {
      getText: (expense) => expense.gstDetail[0].taxPercent,
    },
  },
];
