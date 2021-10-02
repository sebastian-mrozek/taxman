<script lang="ts">
  import type { Column } from "./columns";
  import { isFieldValue, isComponent, isFieldFunction } from "./columns";

  export let item = undefined;
  export let columns: Column[] = [];

  var resolveClass = (column: Column) => {
    return column.alignRight ? "align-right" : "";
  }
</script>

<tr>
  {#if item}
    {#each columns as column}
      <td data-label={column.label} class="{resolveClass(column)}">
        {#if isFieldValue(column.type)}
          {item[column.type.field]}
        {:else if isFieldFunction(column.type)}
          {column.type.getText(item)}
        {:else if isComponent(column.type)}
          <svelte:component this={column.type.component} {...column.type.getProps(item)} />
        {/if}
      </td>
    {/each}
  {/if}
</tr>
<style>
  .align-right {
    text-align: right;
  }
</style>