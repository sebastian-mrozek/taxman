<script lang="ts">
  import type { Period } from "../model";
  import dayjs from "dayjs";

  export let period: Period = undefined;

  $: dateFrom = dayjs(new Date(period.dateFrom)).format("YYYY MMM D");
  $: dateTo = dayjs(new Date(period.dateTo)).format("YYYY MMM D");
</script>

<span>
  {#if period}
    {dateFrom}
    {#if dateFrom != dateTo}
      - {dateTo}
    {/if}
  {/if}
</span>
