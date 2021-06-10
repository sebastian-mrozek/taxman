<script lang="ts">
  import type { Column } from "./columns";
  import ListItemView from "./ListItemView.svelte";

  export let items: any[] = [];
  export let columns: Column[] = [];
</script>

<div class="list">
  {#if items}
    <table class="striped no-max">
      <thead>
        <tr>
          {#each columns as column}
            <th>{column.label}</th>
          {/each}
        </tr>
      </thead>
      <tbody>
        {#each items as item}
          <ListItemView {item} {columns} />
        {/each}
      </tbody>
    </table>
  {/if}
</div>

<style>
  .no-max {
    max-height: none;
  }
</style>
