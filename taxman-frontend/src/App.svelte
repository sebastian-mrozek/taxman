<script lang="ts">
    import TaxReturnView from "./components/TaxReturnView.svelte";
  import type { TaxReturn, TaxYear } from "./model";
  import { service } from "./service";
  import ListView from "./components/ListView.svelte"
  import { INVOICE_COLS } from "./components/columns-invoice";
  import { EXPENSE_COLS } from "./components/columns-expense";
  import { DONATION_COLS } from "./components/columns-donations";

  let years: string[] = ["2019", "2020", "2021", "2022"];
  let currentYear = years[years.length - 1];

  let views: string[] = ["Invoices", "Expenses", "Donations", "Tax Return"];
  let currentView = views[0];

  let taxYear: TaxYear = undefined;
  let taxReturn: TaxReturn = undefined;

  let viewSelected = (e: MouseEvent) => {
    currentView = e.target.textContent;
  };

  let yearSelected = (e: MouseEvent) => {
    currentYear = e.target.textContent;
    service.getTaxYear(currentYear, (data) => (taxYear = data));
    service.getTaxReturn(currentYear, (data) => (taxReturn = data));
  };
  service.getTaxYear(currentYear, (data) => (taxYear = data));
  service.getTaxReturn(currentYear, (data) => (taxReturn = data));
</script>

<main>
  <nav>
    <div class="flow-left">
      {#each views as view}
        <button on:click={viewSelected} class:active={view === currentView}>{view}</button>
      {/each}
    </div>
    <div class="flow-right">
      {#each years as year}
        <button on:click={yearSelected} class:active={year === currentYear}>{year}</button>
      {/each}
    </div>
  </nav>
  <div class="clear">
  {#if taxYear}
    {#if currentView === 'Invoices'}
      <ListView items={taxYear.invoices} columns={INVOICE_COLS} title="Invoices - detail" />
    {:else if currentView === 'Expenses'}
      <ListView items={taxYear.expenses} columns={EXPENSE_COLS} title="Expenses - detail" />
    {:else if currentView === 'Donations'}
      <ListView items={taxYear.donations} columns={DONATION_COLS} title="Donations - detail" />
    {:else if currentView === 'Tax Return'}
      <TaxReturnView {taxReturn} />
    {/if}
  {/if}
</div>
</main>

<style>
  .clear {
    clear: both;
  }
  .active {
    font-weight: bold;
    color: white;
    background-color: navy;
  }
  .flow-left {
    float: left;
  }
  .flow-right {
    float: right;
  }
</style>
