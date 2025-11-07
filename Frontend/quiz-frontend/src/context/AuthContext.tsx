import React, { createContext, useState, useEffect, type ReactNode } from 'react';
import { authService } from '../services/authService';
import type { User, LoginRequest, SignupRequest } from '../types';



interface AuthContextType {
  user: User | null;
  loading: boolean;
  login: (data: LoginRequest) => Promise<void>;
  signup: (data: SignupRequest) => Promise<void>;
  logout: () => void;
  isAuthenticated: boolean;
}

// eslint-disable-next-line react-refresh/only-export-components
export const AuthContext = createContext<AuthContextType>({
  user: null,
  loading: true,
  login: async () => {},
  signup: async () => {},
  logout: () => {},
  isAuthenticated: false,
});

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const initAuth = async () => {
      const currentUser = authService.getCurrentUser();
      if (currentUser) {
        const isValid = await authService.validateToken();
        if (isValid) {
          setUser({
            id: currentUser.id,
            username: currentUser.username,
            email: currentUser.email,
            token: currentUser.token,
            roles: currentUser.roles,
          });
        } else {
          authService.logout();
        }
      }
      setLoading(false);
    };

    initAuth();
  }, []);

  const login = async (data: LoginRequest) => {
    try {
      const response = await authService.signin(data);
      setUser({
        id: response.id,
        username: response.username,
        email: response.email,
        token: response.token,
        roles: response.roles,
      });
    } catch (error) {
      throw error;
    }
  };

  const signup = async (data: SignupRequest) => {
    // eslint-disable-next-line no-useless-catch
    try {
      const response = await authService.signup(data);
      setUser({
        id: response.id,
        username: response.username,
        email: response.email,
        token: response.token,
        roles: response.roles,
      });
    } catch (error) {
      throw error;
    }
  };

  const logout = () => {
    authService.logout();
    setUser(null);
  };

  const value: AuthContextType = {
    user,
    loading,
    login,
    signup,
    logout,
    isAuthenticated: !!user,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};