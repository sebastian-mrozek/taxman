import currency from "currency.js";
import dayjs from "dayjs";
import type { Column } from "./columns";
import TaxView from "./TaxView.svelte";

export const INVOICE_INCOME_COLS: Column[] = [
  { label: "Particulars", type: { field: "particulars" } },
  { label: "Net Value", type: { getText: (invoice) => currency(invoice.netValue).format() } },
  {
    label: "Withholding Tax",
    type: {
      component: TaxView,
      getProps: (invoice) => invoice.withholdingTaxDetail,
    },
  },
];
