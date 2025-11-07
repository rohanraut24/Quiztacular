import axios from 'axios';
import { API_ENDPOINTS, STORAGE_KEYS } from '../utils/constants';
import type { LoginRequest, SignupRequest, AuthResponse } from '../types';

// Create axios instance with base configuration
const apiClient = axios.create({
  baseURL: API_ENDPOINTS.AUTH.SIGNIN.replace('/api/auth/signin', ''),
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add token to headers
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle errors
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expired or invalid
      localStorage.removeItem(STORAGE_KEYS.USER);
      localStorage.removeItem(STORAGE_KEYS.TOKEN);
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

class AuthService {
  /**
   * Sign up new user
   */
  async signup(data: SignupRequest): Promise<AuthResponse> {
    const response = await apiClient.post<AuthResponse>(
      API_ENDPOINTS.AUTH.SIGNUP,
      data
    );
    
    // Store token and user data
    if (response.data.token) {
      localStorage.setItem(STORAGE_KEYS.TOKEN, response.data.token);
      localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(response.data));
    }
    
    return response.data;
  }

  /**
   * Sign in existing user
   */
  async signin(data: LoginRequest): Promise<AuthResponse> {
    const response = await apiClient.post<AuthResponse>(
      API_ENDPOINTS.AUTH.SIGNIN,
      data
    );
    
    // Store token and user data
    if (response.data.token) {
      localStorage.setItem(STORAGE_KEYS.TOKEN, response.data.token);
      localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(response.data));
    }
    
    return response.data;
  }

  /**
   * Validate current token
   */
  async validateToken(): Promise<boolean> {
    try {
      const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
      if (!token) return false;

      await apiClient.get(API_ENDPOINTS.AUTH.VALIDATE);
      return true;
    } catch (error) {
      console.error('Token validation error:', error);
      return false;
    }
  }

  /**
   * Logout user
   */
  logout(): void {
    localStorage.removeItem(STORAGE_KEYS.USER);
    localStorage.removeItem(STORAGE_KEYS.TOKEN);
  }

  /**
   * Get current user from localStorage
   */
  getCurrentUser(): AuthResponse | null {
    const userStr = localStorage.getItem(STORAGE_KEYS.USER);
    if (!userStr) return null;
    
    try {
      return JSON.parse(userStr);
    } catch {
      return null;
    }
  }

  /**
   * Get current token
   */
  getToken(): string | null {
    return localStorage.getItem(STORAGE_KEYS.TOKEN);
  }

  /**
   * Check if user is authenticated
   */
  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}

export const authService = new AuthService();
export default authService;