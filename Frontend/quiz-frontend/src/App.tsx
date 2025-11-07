import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { useAuth } from './hooks/useAuth';
import {Navbar} from './components/Navbar';

// Import pages
import LandingPage from './pages/LandingPage';
import LoginPage from './pages/LoginPage';
import {SignupPage} from './pages/SignupPage';
import {Dashboard} from './pages/Dashboard';
import {SoloQuizSetup} from './pages/SoloQuizSetup';
import SoloQuizPlay from './pages/SoloQuizPlay';
import {MultiplayerBrowser} from './pages/MultiplayerBrowser';
import {CreateRoom} from './pages/CreateRoom';
import HistoryPage from './pages/HistoryPage';
import {LeaderboardPage} from './pages/LeaderboardPage';
import {ProfilePage} from './pages/ProfilePage';

// Protected Route Component
const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900">
        <div className="text-white text-2xl">Loading...</div>
      </div>
    );
  }

  return isAuthenticated ? <>{children}</> : <Navigate to="/login" />;
};

function AppRoutes() {
  const { isAuthenticated } = useAuth();

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900">
      {isAuthenticated && <Navbar />}
      
      <Routes>
        <Route 
          path="/" 
          element={isAuthenticated ? <Navigate to="/dashboard" /> : <LandingPage />} 
        />
        <Route 
          path="/login" 
          element={isAuthenticated ? <Navigate to="/dashboard" /> : <LoginPage />} 
        />
        <Route 
          path="/signup" 
          element={isAuthenticated ? <Navigate to="/dashboard" /> : <SignupPage />} 
        />
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/solo-quiz"
          element={
            <ProtectedRoute>
              <SoloQuizSetup />
            </ProtectedRoute>
          }
        />
        <Route
          path="/solo-quiz/play"
          element={
            <ProtectedRoute>
              <SoloQuizPlay />
            </ProtectedRoute>
          }
        />
        <Route
          path="/multiplayer"
          element={
            <ProtectedRoute>
              <MultiplayerBrowser />
            </ProtectedRoute>
          }
        />
        <Route
          path="/multiplayer/create"
          element={
            <ProtectedRoute>
              <CreateRoom />
            </ProtectedRoute>
          }
        />
        <Route
          path="/history"
          element={
            <ProtectedRoute>
              <HistoryPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/leaderboard"
          element={
            <ProtectedRoute>
              <LeaderboardPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/profile"
          element={
            <ProtectedRoute>
              <ProfilePage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </div>
  );
}

function App() {
  return (
    <AuthProvider>
      <Router>
        <AppRoutes />
      </Router>
    </AuthProvider>
  );
}

export default App;