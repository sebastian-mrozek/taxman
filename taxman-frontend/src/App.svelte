<script lang="ts">
  import type { TaxYear } from "./model";
  import { service } from "./service";
  import ListView from "./components/ListView.svelte";
  import { INVOICE_COLS } from "./components/columns-invoice";
  import { EXPENSE_COLS } from "./components/columns-expense";
  import { DONATION_COLS } from "./components/columns-donations";
  let taxYear: TaxYear = undefined;

  service.get("2021", (data) => (taxYear = data));
</script>

<div>
  {#if taxYear !== undefined}
    Tax Year: {taxYear && taxYear.setup.label}
    <ListView items={taxYear.invoices} columns={INVOICE_COLS} title="Invoices" />
    <ListView items={taxYear.expenses} columns={EXPENSE_COLS} title="Expenses" />
    <ListView items={taxYear.donations} columns={DONATION_COLS} title="Donations" />
  {/if}
</div>

<style>
</style>
