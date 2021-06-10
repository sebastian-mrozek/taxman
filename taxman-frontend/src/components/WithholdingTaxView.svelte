<script lang="ts">
  import type { WithholdingTax } from "../model";
  export let withholdingTax: WithholdingTax;
</script>

<span>
  {#if withholdingTax !== undefined}
    {withholdingTax.taxableAmount} @ {withholdingTax.percentage} %
  {/if}
</span>
