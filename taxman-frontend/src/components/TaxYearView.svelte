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
    Tax Year: {taxYear && taxYear.setup.label}
    <ListView items={taxYear.invoices} columns={INVOICE_COLS} title="Invoices" />
    <ListView items={taxYear.expenses} columns={EXPENSE_COLS} title="Expenses" />
    <ListView items={taxYear.donations} columns={DONATION_COLS} title="Donations" />
  {/if}
</div>
