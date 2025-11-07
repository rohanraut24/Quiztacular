import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Zap, Play, Users, Trophy } from 'lucide-react';

const LandingPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen flex items-center justify-center px-4 bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900">
      <div className="max-w-6xl mx-auto text-center">
        <div className="mb-8 animate-bounce">
          <Zap className="w-24 h-24 mx-auto text-purple-400" />
        </div>
        
        <h1 className="text-6xl md:text-8xl font-bold mb-6 bg-gradient-to-r from-purple-400 via-pink-400 to-purple-400 bg-clip-text text-transparent animate-pulse">
          Quiztacular
        </h1>
        
        <p className="text-xl md:text-2xl text-gray-300 mb-12 max-w-2xl mx-auto">
          Challenge yourself with solo quizzes or compete with friends in real-time multiplayer battles!
        </p>

        <div className="flex flex-col sm:flex-row gap-4 justify-center mb-16">
          <button
            onClick={() => navigate('/login')}
            className="px-8 py-4 bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white rounded-xl font-semibold text-lg transition-all transform hover:scale-105 shadow-lg shadow-purple-500/50"
          >
            Get Started
          </button>
          <button
            onClick={() => navigate('/signup')}
            className="px-8 py-4 bg-slate-800/50 hover:bg-slate-800 text-white rounded-xl font-semibold text-lg transition-all transform hover:scale-105 border border-purple-500/30"
          >
            Create Account
          </button>
        </div>

        <div className="grid md:grid-cols-3 gap-8 max-w-4xl mx-auto">
          <div className="bg-slate-800/30 backdrop-blur-sm p-6 rounded-xl border border-purple-500/20">
            <Play className="w-12 h-12 mx-auto mb-4 text-blue-400" />
            <h3 className="text-xl font-bold text-white mb-2">Solo Mode</h3>
            <p className="text-gray-400">Practice and improve your knowledge across various categories</p>
          </div>
          
          <div className="bg-slate-800/30 backdrop-blur-sm p-6 rounded-xl border border-purple-500/20">
            <Users className="w-12 h-12 mx-auto mb-4 text-green-400" />
            <h3 className="text-xl font-bold text-white mb-2">Multiplayer</h3>
            <p className="text-gray-400">Compete in real-time with players worldwide</p>
          </div>
          
          <div className="bg-slate-800/30 backdrop-blur-sm p-6 rounded-xl border border-purple-500/20">
            <Trophy className="w-12 h-12 mx-auto mb-4 text-yellow-400" />
            <h3 className="text-xl font-bold text-white mb-2">Leaderboards</h3>
            <p className="text-gray-400">Climb the ranks and prove your mastery</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LandingPage;