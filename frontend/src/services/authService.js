import api from "@/utils/api";

export const login = async ({ email, password }) => {
  const { data } = await api.post("/auth/login", { email, password });
  return data; // expects { token: "..." }
};

export const register = async (payload) => {
  // convert payload to FormData for MULTIPART_FORM_DATA_VALUE multipart/form-data submission in backend controller
  const formData = new FormData();

  // add all payload fields to formData
  Object.keys(payload).forEach((key) => {
    formData.append(key, payload[key]);
  });

  const { data } = await api.post("/auth/register", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
  return data;
};

// /logout – POST raw Bearer token as text/plain
export const logout = async (token) =>
  api.post("/auth/logout", token, {
    headers: { "Content-Type": "text/plain" },
  });
