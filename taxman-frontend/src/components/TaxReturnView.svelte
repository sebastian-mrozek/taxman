<script lang="ts">
  import currency from "currency.js";

  import type { TaxReturn } from "../model";
  import { GST_RETURN_COLS } from "./columns-gst-return";
  import ListView from "./ListView.svelte";

  export let taxReturn: TaxReturn = undefined;
</script>

<div>
  {#if taxReturn}
    <div>Net Income: {currency(taxReturn.netIncome).format()}</div>
    <div>Net Expenses: {currency(taxReturn.netExpenses).format()}</div>
    <div>Profit: {currency(taxReturn.profit).format()}</div>
    <ListView items={taxReturn.gstReturns} columns={GST_RETURN_COLS} title="Gst Returns" />
  {/if}
</div>

<style>
</style>
