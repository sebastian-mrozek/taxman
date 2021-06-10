<script lang="ts">
  import type { TaxYear } from "./model";
  import { service } from "./service";
  import ListView from "./components/ListView.svelte";
  import { INVOICE_COLS } from "./components/columns-invoice";
  import { EXPENSE_COLS } from "./components/columns-expense";
  let taxYear: TaxYear = undefined;

  service.get("2021", (data) => (taxYear = data));
</script>

<div>
  {#if taxYear !== undefined}
    Tax Year: {taxYear && taxYear.setup.label}
    <div>
      Invoices
      <ListView items={taxYear.invoices} columns={INVOICE_COLS} />
    </div>
    <div>
      Expenses
      <ListView items={taxYear.expenses} columns={EXPENSE_COLS} />
    </div>
  {/if}
</div>

<style>
</style>
