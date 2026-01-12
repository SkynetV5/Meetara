import Axios, { AxiosError } from 'axios';
const BASE_URL = import.meta.env.VITE_AXIOS_BASE_URL_API;

export const instance = Axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

export const axios = (config, options) => {
  const source = Axios.CancelToken.source();
  const promise = instance({
    ...config,
    ...options,
    cancelToken: source.token,
  }).then(({ data }) => data);

  // @ts-ignore
  promise.cancel = () => {
    source.cancel('Query was cancelled');
  };

  return promise;
};