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
      <span class="labeled-label">Profit: <span class="labeled-value">{currency(taxReturn.profit).format()}</span></span>
      <span class="labeled-label">Income tax: <span class="labeled-value">{currency(taxReturn.incomeTax).format()}</span></span>
      <span class="labeled-label"
        >Remaining income tax: <span class="labeled-value">{currency(taxReturn.remainingIncomeTax).format()}</span></span
      >
      <!-- <span class="labeled-label"
        >Effective tax percent: <span class="labeled-value">{currency(taxReturn.effectiveIncomeTaxPercent).format()}</span></span
      > -->
    </div>
    <hr />
    <ListView items={taxReturn.invoices} columns={INVOICE_INCOME_COLS} title="Income">
      <div slot="footer">
        <span class="labeled-label">Total Income: <span class="labeled-value">{currency(taxReturn.netIncome).format()}</span></span>
        <span class="labeled-label"
          >Total Income Tax Paid: <span class="labeled-value">{currency(taxReturn.incomeTaxPaid).format()}</span></span
        >
      </div>
    </ListView>
    <hr />
    <ListView items={taxReturn.expenseClaims} columns={EXPENSE_CLAIM_COLS} title="Expense Claims">
      <div slot="footer">
        <span class="labeled-label"
          >Total Expenses: <span class="labeled-value">{currency(taxReturn.totalExpensesClaim).format()}</span></span
        >
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
  .labeled-label {
    font-size: 1.7em;
    font-weight: lighter;
    margin: 1em;
  }
  .labeled-value {
    font-family: "Courier New", Courier, monospace;
  }
  .center {
    text-align: center;
  }
</style>
