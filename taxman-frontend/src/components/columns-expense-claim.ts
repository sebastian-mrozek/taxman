import type { Column } from "./columns";
import TaxView from "./TaxView.svelte";
import currency from "currency.js";

export const EXPENSE_CLAIM_COLS: Column[] = [
  { label: "Particulars", type: { getText: (expenseClaim) => expenseClaim.expense.particulars } },
  { label: "Type", type: { getText: (expenseClaim) => expenseClaim.expense.expenseTypeName } },
  { label: "Gross value", type: { getText: (expenseClaim) => currency(expenseClaim.expense.grossValue).format() }, alignRight: true },
  {
    label: "GST",
    type: {
      component: TaxView,
      getProps: (expenseClaim) => expenseClaim.expense.gstDetails[0],
    },
    alignRight: true
  },
  {
    label: "Claim",
    type: {
      component: TaxView,
      getProps: (expenseClaim) => {
        return { taxValue: expenseClaim.claimValue, taxPercent: expenseClaim.claimPercent };
      },
    },
    alignRight: true
  },
];
