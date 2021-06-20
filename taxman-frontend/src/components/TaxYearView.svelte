<script lang="ts">
  import type { TaxYear } from "../model";
  import ListView from "./ListView.svelte";
  import { INVOICE_COLS } from "./columns-invoice";
  import { EXPENSE_COLS } from "./columns-expense";
  import { DONATION_COLS } from "./columns-donations";
  export let taxYear: TaxYear = undefined;
</script>

<div>
  {#if taxYear !== undefined}
    <div class="year-header">Tax Year: {taxYear && taxYear.setup.label}</div>
    <ListView items={taxYear.invoices} columns={INVOICE_COLS} title="Invoices" />
    <ListView items={taxYear.expenses} columns={EXPENSE_COLS} title="Expenses" />
    <ListView items={taxYear.donations} columns={DONATION_COLS} title="Donations" />
  {/if}
</div>

<style>
  .year-header {
    text-align: center;
    font-size: 1.5em;
    font-weight: bold;
  }
</style>
