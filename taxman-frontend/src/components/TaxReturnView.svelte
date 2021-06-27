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
    <div class="year-title">{taxReturn.year} tax year summary</div>
    <div class="center">
      <span class="labeled-value">Profit: {currency(taxReturn.profit).format()}</span>
      <span class="labeled-value">Income tax: {currency(taxReturn.incomeTax).format()}</span>
      <span class="labeled-value">Remaining income tax: {currency(taxReturn.remainingIncomeTax).format()}</span>
      <span class="labeled-value">Effective tax percent: {currency(taxReturn.effectiveIncomeTaxPercent).format()}</span>
    </div>
    <hr />
    <ListView items={taxReturn.invoices} columns={INVOICE_INCOME_COLS} title="Income">
      <div slot="footer">
        <span class="labeled-value">Total Income: {currency(taxReturn.netIncome).format()}</span>
        <span class="labeled-value">Total Income Tax Paid: {currency(taxReturn.incomeTaxPaid).format()}</span>
      </div>
    </ListView>
    <hr />
    <ListView items={taxReturn.expenseClaims} columns={EXPENSE_CLAIM_COLS} title="Expense Claims">
      <div slot="footer">
        <span class="labeled-value">Total Expenses: {currency(taxReturn.totalExpensesClaim).format()}</span>
      </div>
    </ListView>
    <hr />
    <ListView items={taxReturn.gstReturns} columns={GST_RETURN_COLS} title="Gst Returns" />
  {/if}
</div>

<style>
  .year-title {
    font-size: 3em;
    font-weight: lighter;
    text-align: center;
  }
  .labeled-value {
    font-size: 1.7em;
    font-weight: lighter;
    margin: 1em;
  }
  .center {
    text-align: center;
  }
</style>
