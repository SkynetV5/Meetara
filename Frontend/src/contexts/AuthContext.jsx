import {
  createContext,
  useContext,
  useState,
  useEffect,
  useCallback,
} from "react";
import { useLogin } from "../api/endpoints/aPIDocumentation";
import { instance } from "../api/axios";

const AuthContext = createContext(null);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem("token") || null);
  const [isLoading, setIsLoading] = useState(true);

  const loginMutation = useLogin();

  const logout = useCallback(() => {
    setToken(null);
    setUser(null);
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    delete instance.defaults.headers.common["Authorization"];
  }, []);

  // Load user from localStorage on mount
  useEffect(() => {
    const storedToken = localStorage.getItem("token");
    const storedUser = localStorage.getItem("user");

    if (storedToken) {
      setToken(storedToken);
      // Set token in axios instance
      instance.defaults.headers.common[
        "Authorization"
      ] = `Bearer ${storedToken}`;
    }

    if (storedUser) {
      try {
        setUser(JSON.parse(storedUser));
      } catch (e) {
        console.error("Error parsing stored user:", e);
        localStorage.removeItem("user");
      }
    }

    setIsLoading(false);
  }, []);

  // Set up axios interceptor for token
  useEffect(() => {
    const requestInterceptor = instance.interceptors.request.use(
      (config) => {
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    const responseInterceptor = instance.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          // Token expired or invalid
          logout();
        }
        return Promise.reject(error);
      }
    );

    return () => {
      instance.interceptors.request.eject(requestInterceptor);
      instance.interceptors.response.eject(responseInterceptor);
    };
  }, [token, logout]);

  const login = useCallback(
    async (username, password) => {
      try {
        setIsLoading(true);
        const response = await loginMutation.mutateAsync({
          data: { username, password },
        });

        // Extract token from response (adjust based on your API response structure)
        const authToken =
          response?.token || response?.accessToken || response?.access_token;

        if (authToken) {
          setToken(authToken);
          localStorage.setItem("token", authToken);

          // Set token in axios instance
          instance.defaults.headers.common[
            "Authorization"
          ] = `Bearer ${authToken}`;

          // Store user data if available
          if (response?.user) {
            setUser(response.user);
            localStorage.setItem("user", JSON.stringify(response.user));
          } else if (response?.username) {
            // If only username is available, store minimal user object
            const userData = { username: response.username };
            setUser(userData);
            localStorage.setItem("user", JSON.stringify(userData));
          }

          return { success: true };
        } else {
          throw new Error("No token received from server");
        }
      } catch (error) {
        console.error("Login error:", error);
        return {
          success: false,
          error: error.response?.data?.error || error.message || "Login failed",
        };
      } finally {
        setIsLoading(false);
      }
    },
    [loginMutation]
  );

  const isAuthenticated = !!token;

  const value = {
    user,
    token,
    isAuthenticated,
    isLoading: isLoading || loginMutation.isPending,
    login,
    logout,
    error: loginMutation.error,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
