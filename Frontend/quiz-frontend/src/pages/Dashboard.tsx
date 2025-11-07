import {  Award, Play, TrendingUp, Trophy, Users} from 'lucide-react';
import { useAuth } from '../hooks/useAuth';
import { useNavigate } from 'react-router-dom';


export function Dashboard() {
  const { user } = useAuth();
  const navigate = useNavigate();

  return (
    <div className="max-w-7xl mx-auto px-4 py-12">
      <div className="mb-12">
        <h1 className="text-4xl font-bold text-white mb-2">
          Welcome back, <span className="bg-gradient-to-r from-purple-400 to-pink-400 bg-clip-text text-transparent">{user?.username}</span>!
        </h1>
        <p className="text-gray-400 text-lg">Ready to test your knowledge?</p>
      </div>

      {/* Quick Stats */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-12">
        <div className="bg-gradient-to-br from-blue-500/20 to-cyan-500/20 backdrop-blur-sm p-6 rounded-xl border border-blue-500/30">
          <Play className="w-10 h-10 text-blue-400 mb-3" />
          <div className="text-3xl font-bold text-white mb-1">24</div>
          <div className="text-sm text-gray-400">Quizzes Completed</div>
        </div>

        <div className="bg-gradient-to-br from-green-500/20 to-emerald-500/20 backdrop-blur-sm p-6 rounded-xl border border-green-500/30">
          <Award className="w-10 h-10 text-green-400 mb-3" />
          <div className="text-3xl font-bold text-white mb-1">85%</div>
          <div className="text-sm text-gray-400">Avg Score</div>
        </div>

        <div className="bg-gradient-to-br from-purple-500/20 to-pink-500/20 backdrop-blur-sm p-6 rounded-xl border border-purple-500/30">
          <Trophy className="w-10 h-10 text-purple-400 mb-3" />
          <div className="text-3xl font-bold text-white mb-1">#12</div>
          <div className="text-sm text-gray-400">Global Rank</div>
        </div>

        <div className="bg-gradient-to-br from-orange-500/20 to-red-500/20 backdrop-blur-sm p-6 rounded-xl border border-orange-500/30">
          <TrendingUp className="w-10 h-10 text-orange-400 mb-3" />
          <div className="text-3xl font-bold text-white mb-1">+12</div>
          <div className="text-sm text-gray-400">Win Streak</div>
        </div>
      </div>

      {/* Game Modes */}
      <div className="grid md:grid-cols-2 gap-8">
        <div 
          onClick={() => navigate('/solo-quiz/setup')}
          className="group cursor-pointer bg-gradient-to-br from-blue-500/10 to-cyan-500/10 hover:from-blue-500/20 hover:to-cyan-500/20 backdrop-blur-sm p-8 rounded-2xl border border-blue-500/30 transition-all transform hover:scale-105 hover:shadow-2xl hover:shadow-blue-500/20"
        >
          <Play className="w-16 h-16 text-blue-400 mb-4 group-hover:scale-110 transition-transform" />
          <h2 className="text-3xl font-bold text-white mb-3">Solo Quiz</h2>
          <p className="text-gray-400 mb-6">
            Challenge yourself with questions across multiple categories. Practice and improve your skills at your own pace.
          </p>
          <div className="flex items-center text-blue-400 font-semibold">
            Start Solo Quiz
            <span className="ml-2 group-hover:translate-x-2 transition-transform">→</span>
          </div>
        </div>

        <div 
          onClick={() => navigate('/multiplayer')}
          className="group cursor-pointer bg-gradient-to-br from-purple-500/10 to-pink-500/10 hover:from-purple-500/20 hover:to-pink-500/20 backdrop-blur-sm p-8 rounded-2xl border border-purple-500/30 transition-all transform hover:scale-105 hover:shadow-2xl hover:shadow-purple-500/20"
        >
          <Users className="w-16 h-16 text-purple-400 mb-4 group-hover:scale-110 transition-transform" />
          <h2 className="text-3xl font-bold text-white mb-3">Multiplayer</h2>
          <p className="text-gray-400 mb-6">
            Compete with players in real-time! Join rooms or create your own and battle for the top spot.
          </p>
          <div className="flex items-center text-purple-400 font-semibold">
            Join Multiplayer
            <span className="ml-2 group-hover:translate-x-2 transition-transform">→</span>
          </div>
        </div>
      </div>
    </div>
  );
}