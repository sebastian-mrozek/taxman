import axios from "axios";
import type { TaxYear } from "./model";

interface TaxYearService {
  get(label: string, onSuccess: (data: TaxYear) => void): void;
}

export const service: TaxYearService = ((): TaxYearService => {
  //   const restApiClient = axios.create({ baseURL: "tax-year" });
  const restApiClient = axios.create({ baseURL: "http://localhost:7000/api/tax-year" });

  async function get(label: string, onSuccess: (data: TaxYear) => void) {
    restApiClient.get<TaxYear>(label).then((response) => {
      console.log(response);
      onSuccess(response.data);
    });
  }

  return {
    get,
  };
})();
