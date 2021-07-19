<script lang="ts">
  import TaxYearView from "./components/TaxYearView.svelte";
  import TaxReturnView from "./components/TaxReturnView.svelte";
  import type { TaxReturn, TaxYear } from "./model";
  import { service } from "./service";

  let taxYear: TaxYear = undefined;
  let taxReturn: TaxReturn = undefined;

  let handleClick = (e) => {
    service.getTaxYear(e.target.textContent, (data) => (taxYear = data));
    service.getTaxReturn(e.target.textContent, (data) => (taxReturn = data));
  };
  service.getTaxYear("2021", (data) => (taxYear = data));
  service.getTaxReturn("2021", (data) => (taxReturn = data));
</script>

<nav>
  <button on:click={handleClick}>2019</button>
  <button on:click={handleClick}>2020</button>
  <button on:click={handleClick}>2021</button>
</nav>

{#if taxYear}
  <TaxReturnView {taxReturn} />
  <TaxYearView {taxYear} />
{/if}
