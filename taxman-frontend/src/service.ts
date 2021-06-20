import axios from "axios";
import type { TaxYear, GstReturn } from "./model";

interface TaxYearService {
  getTaxYear(label: string, onSuccess: (data: TaxYear) => void): void;
  getGstReturns(label: string, onSuccess: (data: GstReturn[]) => void): void;
}

export const service: TaxYearService = ((): TaxYearService => {
  //   const restApiClient = axios.create({ baseURL: "tax-year" });
  const restApiClient = axios.create({ baseURL: "http://localhost:7000/api/tax-year" });

  async function getTaxYear(label: string, onSuccess: (data: TaxYear) => void) {
    restApiClient.get<TaxYear>(label).then((response) => {
      console.log(response);
      onSuccess(response.data);
    });
  }

  async function getGstReturns(label: string, onSuccess: (data: GstReturn[]) => void) {
    restApiClient.get<GstReturn[]>(label + "/gst-returns").then((response) => {
      console.log(response);
      onSuccess(response.data);
    });
  }

  return {
    getTaxYear,
    getGstReturns,
  };
})();
