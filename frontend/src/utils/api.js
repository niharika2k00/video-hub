import axios from "axios";
import { toast } from "react-toastify";

// Use environment variable, fallback to relative URLs (means the endpoint of the backend server) if not set
const baseURL = import.meta.env.VITE_BACKEND_SERVER_URL
  ? `${import.meta.env.VITE_BACKEND_SERVER_URL}/api`
  : "/api";

const api = axios.create({
  baseURL: baseURL,
  timeout: 10000,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  (res) => res,
  (err) => {
    toast.error(err?.response?.data?.message || "Something went wrong");
    return Promise.reject(err);
  }
);

export default api;
