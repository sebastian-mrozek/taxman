<script lang="ts">
  import currency from "currency.js";

  import type { TaxReturn } from "../model";
  import { GST_RETURN_COLS } from "./columns-gst-return";
  import { INVOICE_INCOME_COLS } from "./columns-invoice-income";
  import { EXPENSE_CLAIM_COLS } from "./columns-expense-claim";
  import ListView from "./ListView.svelte";

  export let taxReturn: TaxReturn = undefined;
</script>

<div>
  {#if taxReturn}
    <div class="profit-summary">
      <span class="labeled-value">Income: {currency(taxReturn.netIncome).format()}</span>
      <span class="labeled-value">Expenses: {currency(taxReturn.totalExpensesClaim).format()}</span>
      <span class="labeled-value">Profit: {currency(taxReturn.profit).format()}</span>
      <span class="labeled-value">Tax Paid: {currency(taxReturn.taxPaid).format()}</span>
    </div>
    <ListView items={taxReturn.gstReturns} columns={GST_RETURN_COLS} title="Gst Returns" />
    <ListView items={taxReturn.invoices} columns={INVOICE_INCOME_COLS} title="Income" />
    <ListView items={taxReturn.expenseClaims} columns={EXPENSE_CLAIM_COLS} title="Expense Claims" />
  {/if}
</div>

<style>
  .profit-summary {
    text-align: center;
  }
  .labeled-value {
    font-size: 1.7em;
    font-weight: lighter;
    margin: 1em;
  }
</style>
