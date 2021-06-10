<script lang="ts">
  import App from "../App.svelte";

  import type { Period } from "../model";

  export let period: Period = undefined;

  $: dateFrom = new Date(period.dateFrom).toLocaleDateString();
  $: dateTo = new Date(period.dateTo).toLocaleDateString();
</script>

<span>
  {#if period}
    {dateFrom}
    {#if dateFrom != dateTo}
      - {dateTo}
    {/if}
  {/if}
</span>
