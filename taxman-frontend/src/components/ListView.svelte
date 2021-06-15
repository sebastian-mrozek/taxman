<script lang="ts">
  import type { Column } from "./columns";
  import ListItemView from "./ListItemView.svelte";

  export let items: any[] = [];
  export let columns: Column[] = [];
  export let title: string;
</script>

<div class="panel shadowed rounded bordered">
  {#if title}<div class="card-title">{title}</div>{/if}
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
  .panel {
    padding: 1em;
    margin: 1em;
  }
  .card-title {
    text-align: center;
    font-size: 1.2em;
    font-weight: bold;
    color: #777;
    font-variant: small-caps;
    margin-left: 1em;
  }
</style>
