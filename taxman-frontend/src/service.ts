import axios from "axios";
import type { TaxYear, TaxReturn } from "./model";

interface TaxYearService {
  getTaxYear(label: string, onSuccess: (data: TaxYear) => void): void;
  getTaxReturn(label: string, onSuccess: (data: TaxReturn) => void): void;
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

  async function getTaxReturn(label: string, onSuccess: (data: TaxReturn) => void) {
    restApiClient.get<TaxReturn>(label + "/tax-return").then((response) => {
      console.log(response);
      onSuccess(response.data);
    });
  }

  return {
    getTaxYear,
    getTaxReturn,
  };
})();
