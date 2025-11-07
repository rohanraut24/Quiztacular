import { Award, Star, Trophy } from "lucide-react";
import { useAuth } from "../hooks/useAuth";

export function ProfilePage() {
  const { user } = useAuth();

  return (
    <div className="max-w-4xl mx-auto px-4 py-12">
      <div className="bg-slate-800/30 backdrop-blur-sm p-8 rounded-2xl border border-purple-500/20">
        <div className="flex items-center space-x-6 mb-8">
          <div className="w-24 h-24 bg-gradient-to-r from-purple-500 to-pink-500 rounded-full flex items-center justify-center text-4xl">
            👤
          </div>
          <div>
            <h1 className="text-3xl font-bold text-white mb-2">{user?.username}</h1>
            <p className="text-gray-400">{user?.email}</p>
          </div>
        </div>

        <div className="grid md:grid-cols-3 gap-6">
          <div className="bg-slate-900/50 p-6 rounded-xl text-center">
            <Trophy className="w-10 h-10 text-yellow-400 mx-auto mb-3" />
            <div className="text-2xl font-bold text-white mb-1">24</div>
            <div className="text-sm text-gray-400">Total Quizzes</div>
          </div>

          <div className="bg-slate-900/50 p-6 rounded-xl text-center">
            <Award className="w-10 h-10 text-green-400 mx-auto mb-3" />
            <div className="text-2xl font-bold text-white mb-1">85%</div>
            <div className="text-sm text-gray-400">Avg Score</div>
          </div>

          <div className="bg-slate-900/50 p-6 rounded-xl text-center">
            <Star className="w-10 h-10 text-purple-400 mx-auto mb-3" />
            <div className="text-2xl font-bold text-white mb-1">#12</div>
            <div className="text-sm text-gray-400">Global Rank</div>
          </div>
        </div>
      </div>
    </div>
  );
}