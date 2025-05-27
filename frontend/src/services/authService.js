import api from "@/utils/api";

export const login = async ({ email, password }) => {
  const { data } = await api.post("/auth/login", { email, password });
  return data; // expects { token: "..." }
};

export const register = async (payload) => {
  const { data } = await api.post("/auth/register", payload);
  return data; // expects { token: "..." }
};

/**
 * logoutApi – POST raw Bearer token as text/plain
 */
export const logoutApi = async (token) =>
  api.post("/auth/logout", token, {
    headers: { "Content-Type": "text/plain" },
  });
