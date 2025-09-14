import { createContext, useContext, useState, useEffect } from "react";
import { jwtDecode } from "jwt-decode";
import {
  login as apiLogin,
  register as apiRegister,
} from "@/services/authService";
import api from "@/utils/api";
import { analytics } from "@/utils/analytics";

const AuthContext = createContext();

const parseToken = (token) => {
  if (!token) return null;

  try {
    return jwtDecode(token);
  } catch (error) {
    console.error("Error parsing token:", error);
    return null;
  }
};

const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchUserDetails = async (userId) => {
    try {
      const response = await api.get(`/users/${userId}`);
      return response.data;
    } catch (error) {
      console.error("Error fetching user details:", error);
      throw error;
    }
  };

  const initializeAuth = async () => {
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        setIsLoading(false);
        return;
      }

      const payload = parseToken(token);
      if (!payload) {
        localStorage.removeItem("token");
        setIsLoading(false);
        return;
      }

      api.defaults.headers.common.Authorization = `Bearer ${token}`;
      const userDetails = await fetchUserDetails(payload.sub);
      setUser(userDetails);
    } catch (error) {
      setError(error.message);
      localStorage.removeItem("token");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    initializeAuth();
  }, []);

  const login = async (email, password) => {
    try {
      setError(null);
      const responseObj = await apiLogin({ email, password });
      const { token } = responseObj; // destructuring the response object to get the token
      console.log("token", responseObj);
      localStorage.setItem("token", token);
      api.defaults.headers.common.Authorization = `Bearer ${token}`;

      const payload = parseToken(token);
      console.log("payload", payload);
      if (payload) {
        const userDetails = await fetchUserDetails(payload.sub);
        setUser(userDetails);
      }
    } catch (error) {
      setError(error.message);
      throw error;
    }
  };

  const register = async (payload) => {
    try {
      setError(null);
      const responseObj = await apiRegister(payload);
      console.log("Registration successful:", responseObj);
    } catch (error) {
      setError(error.message);
      throw error;
    }
  };

  const logout = () => {
    analytics.trackSignOut();
    localStorage.removeItem("token");
    delete api.defaults.headers.common.Authorization;
    setUser(null);
    setError(null);
  };

  const value = {
    user,
    isLoading,
    error,
    login,
    register,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

export { AuthProvider };
export default useAuth;
// export { useAuth, AuthProvider };
