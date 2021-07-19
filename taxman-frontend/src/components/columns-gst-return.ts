import currency from "currency.js";
import type { Column } from "./columns";
import PeriodView from "./PeriodView.svelte";

export const GST_RETURN_COLS: Column[] = [
  {
    label: "Period",
    type: {
      component: PeriodView,
      getProps: (gstReturn) => {
        return { period: gstReturn.period };
      },
    },
  },
  { label: "Net Income", type: { getText: (gstReturn) => currency(gstReturn.netIncome).format() } },
  { label: "Net Expenses", type: { getText: (gstReturn) => currency(gstReturn.netExpenses).format() } },
  { label: "GST Collected", type: { getText: (gstReturn) => currency(gstReturn.gstCollected).format() } },
  { label: "GST Paid", type: { getText: (gstReturn) => currency(gstReturn.gstPaid).format() } },
  { label: "GST Balance", type: { getText: (gstReturn) => currency(gstReturn.gstBalance).format() } },
];
