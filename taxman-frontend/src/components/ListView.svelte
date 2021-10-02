<script lang="ts">
  import type { Column } from "./columns";
  import ListItemView from "./ListItemView.svelte";

  export let items: any[] = [];
  export let columns: Column[] = [];
  export let title: string;

  var resolveClass = (column: Column) => {
    return column.alignRight ? "align-right" : "";
  }
</script>

<div class="panel shadowed rounded bordered">
  {#if title}<div class="card-title">{title}</div>{/if}
  {#if items}
    <table class="striped no-max hoverable">
      <thead>
        <tr>
          {#each columns as column}
            <th class="{resolveClass(column)}">{column.label}</th>
          {/each}
        </tr>
      </thead>
      <tbody>
        {#each items as item}
          <ListItemView {item} {columns} />
        {/each}
      </tbody>
      <tfoot>
        <div class="footer-slot">
          <slot name="footer" />
        </div>
      </tfoot>
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
    background-color: aliceblue;
  }
  .card-title {
    text-align: center;
    font-size: 1.5em;
    font-weight: lighter;
    font-variant: small-caps;
  }
  .footer-slot {
    margin-top: 1em;
  }
  .align-right {
    text-align: right;
  }
</style>
