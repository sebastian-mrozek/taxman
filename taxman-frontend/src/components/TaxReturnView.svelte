<script lang="ts">
  import currency from "currency.js";

  import type { TaxReturn } from "../model";
  import { GST_RETURN_COLS } from "./columns-gst-return";
  import ListView from "./ListView.svelte";

  export let taxReturn: TaxReturn = undefined;
</script>

<div>
  {#if taxReturn}
    <div class="profit-summary">
      <span class="labeled-value">Net Income: {currency(taxReturn.netIncome).format()}</span>
      <span class="labeled-value">Net Expenses: {currency(taxReturn.netExpenses).format()}</span>
      <span class="labeled-value">Profit: {currency(taxReturn.profit).format()}</span>
    </div>
    <ListView items={taxReturn.gstReturns} columns={GST_RETURN_COLS} title="Gst Returns" />
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
