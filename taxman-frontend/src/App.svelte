<script lang="ts">
  import TaxYearView from "./components/TaxYearView.svelte";
  import { service } from "./service";

  let taxYear = undefined;

  let handleClick = (e) => {
    service.get(e.target.textContent, (data) => (taxYear = data));
  };
</script>

<nav>
  <button on:click={handleClick}>2019</button>
  <button on:click={handleClick}>2020</button>
  <button on:click={handleClick}>2021</button>
</nav>

<TaxYearView {taxYear} />

<style>
</style>
