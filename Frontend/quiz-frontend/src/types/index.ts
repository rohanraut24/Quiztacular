// User & Auth Types
export interface User {
  id: number;
  username: string;
  email: string;
  token: string;
  roles: string[];
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface SignupRequest {
  username: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  id: number;
  username: string;
  email: string;
  roles: string[];
}

// Quiz Types
export interface Category {
  id: string;
  name: string;
  icon: string;
  color: string;
}

export interface Question {
  questionNumber: number;
  question: string;
  options: string[];
  type: string;
}

export interface QuizAttempt {
  attemptId: number;
  category: string;
  difficulty: string;
  totalQuestions: number;
  questions: Question[];
  startTime: string;
}

export interface QuizSubmission {
  attemptId: number;
  answers: Record<number, string>;
}

export interface QuestionResult {
  questionNumber: number;
  question: string;
  correctAnswer: string;
  userAnswer: string;
  isCorrect: boolean;
}

export interface QuizResult {
  attemptId: number;
  totalQuestions: number;
  correctAnswers: number;
  score: number;
  percentage: number;
  results: QuestionResult[];
}

export interface QuizHistory {
  attemptId: number;
  category: string;
  difficulty: string;
  score: number;
  percentage: number;
  completedAt: string;
}

export interface LeaderboardEntry {
  rank: number;
  username: string;
  score: number;
  quizzesCompleted: number;
}

export interface UserStats {
  totalQuizzes: number;
  averageScore: number;
  totalCorrect: number;
  totalQuestions: number;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  categoryStats: Record<string, any>;
}

// Multiplayer Room Types
export interface Room {
  id: number;
  roomCode: string;
  roomName: string;
  category: string;
  difficulty: string;
  totalQuestions: number;
  maxPlayers: number;
  currentPlayers: number;
  status: 'WAITING' | 'STARTING' | 'IN_PROGRESS' | 'COMPLETED';
  hostUsername?: string;
}

export interface Player {
  username: string;
  currentScore: number;
  isReady: boolean;
  isHost: boolean;
  correctAnswers?: number;
}

export interface RoomState {
  roomCode: string;
  roomName: string;
  category: string;
  difficulty: string;
  status: 'WAITING' | 'STARTING' | 'IN_PROGRESS' | 'COMPLETED';
  players: Player[];
  currentQuestion?: number;
  totalQuestions?: number;
  countdown?: number;
}

export interface CreateRoomRequest {
  roomName: string;
  category: string;
  difficulty: string;
  totalQuestions: number;
  maxPlayers: number;
}

export interface JoinRoomRequest {
  roomCode: string;
}

export interface RoomReadyRequest {
  roomCode: string;
}

export interface SubmitAnswerRequest {
  roomCode: string;
  questionNumber: number;
  answer: string;
  timeTakenMs: number;
}

export interface MultiplayerQuestion {
  questionNumber: number;
  totalQuestions: number;
  question: string;
  options: string[];
  timeLimit: number;
}

export interface AnswerResult {
  username: string;
  questionNumber: number;
  isCorrect: boolean;
  pointsEarned: number;
  totalScore: number;
  timeTaken?: number;
}

export interface LiveLeaderboard {
  rankings: RankingEntry[];
}

export interface RankingEntry {
  rank: number;
  username: string;
  score: number;
  correctAnswers: number;
}

export interface QuizCompleted {
  winnerUsername: string;
  finalRankings: RankingEntry[];
}

// WebSocket Message Types
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export interface WSMessage<T = any> {
  type: string;
  payload: T;
}