<script lang="ts">
  import currency from "currency.js";
  export let taxValue: number;
  export let taxPercent: number;
</script>

<span>
  {#if taxValue}
    <span>{currency(taxValue).format()} </span>
    <span class="label">{taxPercent}%</span>
  {:else}
    <span>{currency(0.0).format()} </span>
  {/if}
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
