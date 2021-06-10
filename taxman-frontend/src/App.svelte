<script lang="ts">
  import type { TaxYear } from "./model";
  import { service } from "./service";
  import ListView from "./components/ListView.svelte";
  import WithholdingTaxView from "./components/WithholdingTaxView.svelte";
  import type { Column } from "./components/columns";

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

  let taxYear: TaxYear = undefined;

  service.get("2021", (data) => (taxYear = data));
</script>

<div>
  {#if taxYear !== undefined}
    Tax Year: {taxYear && taxYear.setup.label}
    <ListView items={taxYear.invoices} columns={INVOICE_COLS} />
  {/if}
</div>

<style>
</style>
