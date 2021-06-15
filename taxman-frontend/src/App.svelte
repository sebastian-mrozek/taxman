<script lang="ts">
  import { dataset_dev } from "svelte/internal";

  import TaxYearView from "./components/TaxYearView.svelte";
  import { service } from "./service";

  let taxYear = undefined;
  service.get("2021", (data) => (taxYear = data));
</script>

<TaxYearView {taxYear} />

<style>
</style>
