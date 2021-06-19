import currency from "currency.js";
import type { Column } from "./columns";
import TaxView from "./TaxView.svelte";

export const INVOICE_COLS: Column[] = [
  { label: "Particulars", type: { field: "particulars" } },
  { label: "Customer", type: { field: "customer" } },
  {
    label: "Date Issued",
    type: {
      getText: (invoice) => new Date(invoice.dateIssued).toLocaleDateString(),
    },
  },
  { label: "Net Value", type: { getText: (invoice) => currency(invoice.netValue).format() } },
  {
    label: "GST",
    type: {
      component: TaxView,
      getProps: (invoice) => invoice.gstDetail,
    },
  },
  {
    label: "Total",
    type: {
      getText: (invoice) => {
        return currency(invoice.total).format();
      },
    },
  },
  {
    label: "Withholding Tax",
    type: {
      component: TaxView,
      getProps: (invoice) => invoice.withholdingTaxDetail,
    },
  },
  {
    label: "To pay",
    type: {
      getText: (invoice) => {
        return currency(invoice.toPay).format();
      },
    },
  },
];
