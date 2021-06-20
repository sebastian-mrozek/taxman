<script lang="ts">
  import TaxYearView from "./components/TaxYearView.svelte";
  import type { GstReturn, TaxYear } from "./model";
  import { service } from "./service";

  let taxYear: TaxYear = undefined;
  let gstReturns: GstReturn[] = undefined;

  let handleClick = (e) => {
    service.getTaxYear(e.target.textContent, (data) => (taxYear = data));
    service.getGstReturns(e.target.textContent, (data) => (gstReturns = data));
  };
  service.getTaxYear("2021", (data) => (taxYear = data));
  service.getGstReturns("2021", (data) => (gstReturns = data));
</script>

<nav>
  <button on:click={handleClick}>2019</button>
  <button on:click={handleClick}>2020</button>
  <button on:click={handleClick}>2021</button>
</nav>

<TaxYearView {taxYear} />

<style>
</style>
