// API Configuration
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

export const API_ENDPOINTS = {
  // Auth Service
  AUTH: {
    SIGNUP: `${API_BASE_URL}/api/auth/signup`,
    SIGNIN: `${API_BASE_URL}/api/auth/signin`,
    VALIDATE: `${API_BASE_URL}/api/auth/validate`,
  },
  
  // Quiz Service
  QUIZ: {
    CATEGORIES: `${API_BASE_URL}/api/quiz/categories`,
    GENERATE: `${API_BASE_URL}/api/quiz/generate`,
    SUBMIT: `${API_BASE_URL}/api/quiz/submit`,
    HISTORY: `${API_BASE_URL}/api/quiz/history`,
    LEADERBOARD: `${API_BASE_URL}/api/quiz/leaderboard`,
    STATS: `${API_BASE_URL}/api/quiz/stats`,
  },
  
  // Room Service
  ROOM: {
    CATEGORIES: `${API_BASE_URL}/api/room/categories`,
    AVAILABLE: `${API_BASE_URL}/api/room/available`,
  },
};

// WebSocket Configuration
export const WS_BASE_URL = import.meta.env.VITE_WS_BASE_URL || 'http://localhost:8080/ws';

export const WS_TOPICS = {
  ROOM_CREATED: '/user/queue/room/created',
  ROOM_UPDATES: (roomCode: string) => `/topic/room/${roomCode}`,
  ROOM_QUESTION: (roomCode: string) => `/topic/room/${roomCode}/question`,
  ROOM_ANSWER: (roomCode: string) => `/topic/room/${roomCode}/answer`,
  ROOM_LEADERBOARD: (roomCode: string) => `/topic/room/${roomCode}/leaderboard`,
  ROOM_COMPLETED: (roomCode: string) => `/topic/room/${roomCode}/completed`,
};

export const WS_DESTINATIONS = {
  ROOM_CREATE: '/app/room/create',
  ROOM_JOIN: '/app/room/join',
  ROOM_READY: '/app/room/ready',
  ROOM_ANSWER: '/app/room/answer',
};

// Local Storage Keys
export const STORAGE_KEYS = {
  USER: 'quiztacular_user',
  TOKEN: 'quiztacular_token',
};

// Quiz Configuration
export const QUIZ_CONFIG = {
  DEFAULT_TIME_PER_QUESTION: 30, // seconds
  MIN_QUESTIONS: 5,
  MAX_QUESTIONS: 20,
  QUESTION_STEP: 5,
};

// Room Configuration
export const ROOM_CONFIG = {
  MIN_PLAYERS: 2,
  MAX_PLAYERS: 10,
  DEFAULT_MAX_PLAYERS: 5,
};

// Categories with Icons
export const CATEGORIES = [
  { 
    id: 'computers', 
    name: 'Computers', 
    icon: '💻', 
    color: 'from-blue-500 to-cyan-500',
    description: 'Technology, programming, and hardware'
  },
  { 
    id: 'science', 
    name: 'Science', 
    icon: '🔬', 
    color: 'from-green-500 to-emerald-500',
    description: 'Physics, chemistry, and biology'
  },
  { 
    id: 'mathematics', 
    name: 'Mathematics', 
    icon: '🔢', 
    color: 'from-purple-500 to-pink-500',
    description: 'Numbers, algebra, and geometry'
  },
  { 
    id: 'history', 
    name: 'History', 
    icon: '📚', 
    color: 'from-orange-500 to-red-500',
    description: 'World events and civilizations'
  },
  { 
    id: 'geography', 
    name: 'Geography', 
    icon: '🌍', 
    color: 'from-teal-500 to-cyan-500',
    description: 'Countries, capitals, and landmarks'
  },
  { 
    id: 'sports', 
    name: 'Sports', 
    icon: '⚽', 
    color: 'from-yellow-500 to-orange-500',
    description: 'Athletics, games, and competitions'
  },
];

// Difficulties
export const DIFFICULTIES = [
  { value: 'easy', label: 'Easy', color: 'text-green-400' },
  { value: 'medium', label: 'Medium', color: 'text-yellow-400' },
  { value: 'hard', label: 'Hard', color: 'text-red-400' },
];

// Routes
export const ROUTES = {
  LANDING: '/',
  LOGIN: '/login',
  SIGNUP: '/signup',
  DASHBOARD: '/dashboard',
  SOLO_SETUP: '/solo-quiz',
  SOLO_PLAY: '/solo-quiz/play',
  SOLO_RESULTS: '/solo-quiz/results',
  MULTIPLAYER: '/multiplayer',
  CREATE_ROOM: '/multiplayer/create',
  WAITING_ROOM: '/multiplayer/room/:code',
  MULTIPLAYER_PLAY: '/multiplayer/room/:code/play',
  MULTIPLAYER_RESULTS: '/multiplayer/room/:code/results',
  HISTORY: '/history',
  LEADERBOARD: '/leaderboard',
  PROFILE: '/profile',
};