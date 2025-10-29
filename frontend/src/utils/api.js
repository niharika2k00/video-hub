import axios from "axios";
import { toast } from "react-toastify";

// Environment-based URL configuration
const getBaseURL = () => {
  if (import.meta.env.VITE_APP_ENV === "development") {
    // Development: Use explicit backend server URL
    return import.meta.env.VITE_BACKEND_SERVER_URL
      ? `${import.meta.env.VITE_BACKEND_SERVER_URL}/api`
      : "/api";
  } else {
    // Production: Use relative URL (same domain)
    return "/api";
  }
};

const baseURL = getBaseURL();

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
