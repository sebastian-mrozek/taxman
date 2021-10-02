import currency from "currency.js";
import dayjs from "dayjs";
import type { Column } from "./columns";
import TaxView from "./TaxView.svelte";

export const INVOICE_COLS: Column[] = [
  { label: "Particulars", type: { field: "particulars" } },
  { label: "Customer", type: { field: "customer" } },
  {
    label: "Date Issued",
    type: {
      getText: (invoice) => dayjs(new Date(invoice.dateIssued)).format("YYYY MMM D"),
    },
  },
  {
    label: "Date Paid",
    type: {
      getText: (invoice) => dayjs(new Date(invoice.datePaid)).format("YYYY MMM D"),
    },
  },
  { 
    label: "Net Value", 
    type: { getText: (invoice) => currency(invoice.netValue).format() },
    alignRight: true
  },
  {
    label: "GST",
    type: {
      component: TaxView,
      getProps: (invoice) => invoice.gstDetail,
    },
    alignRight: true
  },
  {
    label: "Total",
    type: {
      getText: (invoice) => {
        return currency(invoice.total).format();
      },
    },
    alignRight: true
  },
  {
    label: "Withholding Tax",
    type: {
      component: TaxView,
      getProps: (invoice) => invoice.withholdingTaxDetail,
    },
    alignRight: true
  },
  {
    label: "To pay",
    type: {
      getText: (invoice) => {
        return currency(invoice.toPay).format();
      },
    },
    alignRight: true
  },
];
