import currency from "currency.js";
import type { Column } from "./columns";
import PeriodView from "./PeriodView.svelte";

export const DONATION_COLS: Column[] = [
  { label: "Organisation", type: { field: "organisation" } },
  { label: "Registration No", type: { field: "registrationNo" } },
  {
    label: "Period",
    type: {
      component: PeriodView,
      getProps: (donation) => {
        return { period: donation.period };
      },
    },
  },
  { label: "Value", type: { getText: (donation) => currency(donation.value).format() }, alignRight: true },
];
