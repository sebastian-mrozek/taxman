import type { Column } from "./columns";
import WithholdingTaxView from "./WithholdingTaxView.svelte";

export const INVOICE_COLS: Column[] = [
  { label: "Particulars", type: { field: "particulars" } },
  { label: "Customer", type: { field: "customer" } },
  { label: "Date Issued", type: { field: "dateIssued" } },
  { label: "Net Value", type: { field: "netValue" } },
  {
    label: "GST",
    type: {
      getText: (invoice) => invoice.gstDetail.taxPercent,
    },
  },
  {
    label: "Withholding Tax",
    type: {
      component: WithholdingTaxView,
      getProps: (invoice) => {
        return { withholdingTax: invoice.withholdingTax };
      },
    },
  },
];
