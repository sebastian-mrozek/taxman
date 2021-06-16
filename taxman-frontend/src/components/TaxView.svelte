<script lang="ts">
  import currency from "currency.js";
  export let taxableAmount: number;
  export let taxPercent: number;
</script>

<span>
  <span>{currency(taxableAmount).multiply(currency(taxPercent)).divide(currency(100)).format()} </span>
  <span class="label">{taxPercent}%</span>
</span>

<style>
  .label {
    font-size: 0.8em;
    color: white;
    font-weight: bold;
    background-color: gray;
    border-radius: 3px;
    padding: 0.2em;
  }
</style>
