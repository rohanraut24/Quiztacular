import axios from 'axios';
import { API_ENDPOINTS, STORAGE_KEYS } from '../utils/constants';
import type { Room, Category } from '../types';

const apiClient = axios.create({
  baseURL: API_ENDPOINTS.ROOM.AVAILABLE.replace('/api/room/available', ''),
  headers: {
    'Content-Type': 'application/json',
  },
});

apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

class RoomService {
  async getCategories(): Promise<Category[]> {
    try {
      const response = await apiClient.get<Category[]>(
        API_ENDPOINTS.ROOM.CATEGORIES
      );
      return response.data;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    } catch (error: any) {
      console.error('Get room categories error:', error);
      throw new Error(error.response?.data?.message || 'Failed to fetch categories');
    }
  }

  async getAvailableRooms(): Promise<Room[]> {
    try {
      const response = await apiClient.get<Room[]>(
        API_ENDPOINTS.ROOM.AVAILABLE
      );
      return response.data;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    } catch (error: any) {
      console.error('Get available rooms error:', error);
      throw new Error(error.response?.data?.message || 'Failed to fetch rooms');
    }
  }
}

export const roomService = new RoomService();
export default roomService;