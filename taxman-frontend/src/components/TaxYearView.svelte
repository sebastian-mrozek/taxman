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
    <ListView items={taxYear.invoices} columns={INVOICE_COLS} title="Invoices - detail" />
    <ListView items={taxYear.expenses} columns={EXPENSE_COLS} title="Expenses - detail" />
    <ListView items={taxYear.donations} columns={DONATION_COLS} title="Donations - detail" />
  {/if}
</div>
