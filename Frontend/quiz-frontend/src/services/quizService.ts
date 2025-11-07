import axios from 'axios';
import { API_ENDPOINTS, STORAGE_KEYS } from '../utils/constants';
import type {
  QuizAttempt,
  QuizSubmission,
  QuizResult,
  QuizHistory,
  LeaderboardEntry,
  UserStats,
  Category,
} from '../types';

// Create axios instance
const apiClient = axios.create({
  baseURL: API_ENDPOINTS.QUIZ.CATEGORIES.replace('/api/quiz/categories', ''),
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add token
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

interface GenerateQuizRequest {
  category: string;
  difficulty: string;
  amount: number;
}

interface CategoriesResponse {
  categories: Category[];
  difficulties: string[];
}

class QuizService {
  /**
   * Get available categories
   */
  async getCategories(): Promise<CategoriesResponse> {
    try {
      const response = await apiClient.get<CategoriesResponse>(
        API_ENDPOINTS.QUIZ.CATEGORIES
      );
      return response.data;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    } catch (error: any) {
      console.error('Get categories error:', error);
      throw new Error(error.response?.data?.message || 'Failed to fetch categories');
    }
  }

  /**
   * Generate a new quiz
   */
  async generateQuiz(data: GenerateQuizRequest): Promise<QuizAttempt> {
    try {
      const response = await apiClient.post<QuizAttempt>(
        API_ENDPOINTS.QUIZ.GENERATE,
        data
      );
      return response.data;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    } catch (error: any) {
      console.error('Generate quiz error:', error);
      throw new Error(error.response?.data?.message || 'Failed to generate quiz');
    }
  }

  /**
   * Submit quiz answers
   */
  async submitQuiz(
  submission: QuizSubmission
): Promise<QuizResult> {
  try {
    const response = await apiClient.post<QuizResult>(
      API_ENDPOINTS.QUIZ.SUBMIT,
      submission
    );
    return response.data;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  } catch (error: any) {
    console.error('Submit quiz error:', error);
    throw new Error(error.response?.data?.message || 'Failed to submit quiz');
  }
}


  /**
   * Get user's quiz history
   */
  async getHistory(): Promise<QuizHistory[]> {
    try {
      const response = await apiClient.get<QuizHistory[]>(
        API_ENDPOINTS.QUIZ.HISTORY
      );
      return response.data;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    } catch (error: any) {
      console.error('Get history error:', error);
      throw new Error(error.response?.data?.message || 'Failed to fetch history');
    }
  }

  /**
   * Get leaderboard
   */
  async getLeaderboard(
    category?: string,
    limit: number = 10
  ): Promise<LeaderboardEntry[]> {
    try {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const params: any = { limit };
      if (category) params.category = category;

      const response = await apiClient.get<LeaderboardEntry[]>(
        API_ENDPOINTS.QUIZ.LEADERBOARD,
        { params }
      );
      return response.data;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    } catch (error: any) {
      console.error('Get leaderboard error:', error);
      throw new Error(error.response?.data?.message || 'Failed to fetch leaderboard');
    }
  }

  /**
   * Get user statistics
   */
  async getStats(): Promise<UserStats> {
    try {
      const response = await apiClient.get<UserStats>(
        API_ENDPOINTS.QUIZ.STATS
      );
      return response.data;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    } catch (error: any) {
      console.error('Get stats error:', error);
      throw new Error(error.response?.data?.message || 'Failed to fetch stats');
    }
  }
}

export const quizService = new QuizService();
export default quizService;