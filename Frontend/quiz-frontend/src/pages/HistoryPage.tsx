import React, { useState, useEffect } from 'react';
import { Clock, TrendingUp } from 'lucide-react';
import { quizService } from '../services/quizService';
import type { QuizHistory } from '../types';

const HistoryPage: React.FC = () => {
  const [history, setHistory] = useState<QuizHistory[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadHistory();
  }, []);

  const loadHistory = async () => {
    try {
      const data = await quizService.getHistory();
      setHistory(data);
    } catch (error) {
      console.error('Failed to load history:', error);
    } finally {
      setLoading(false);
    }
  };

  const getScoreColor = (percentage: number) => {
    if (percentage >= 80) return 'text-green-400';
    if (percentage >= 60) return 'text-yellow-400';
    return 'text-red-400';
  };

  const getDifficultyColor = (difficulty: string) => {
    if (difficulty === 'easy') return 'bg-green-500/20 text-green-400';
    if (difficulty === 'medium') return 'bg-yellow-500/20 text-yellow-400';
    return 'bg-red-500/20 text-red-400';
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 pt-20 px-4 pb-12">
      <div className="max-w-6xl mx-auto">
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-white mb-2">Quiz History</h1>
          <p className="text-gray-400">Track your progress and performance</p>
        </div>

        {loading ? (
          <div className="text-center py-16">
            <div className="text-white text-xl">Loading history...</div>
          </div>
        ) : history.length === 0 ? (
          <div className="text-center py-16">
            <Clock className="w-16 h-16 text-gray-600 mx-auto mb-4" />
            <p className="text-gray-400 text-lg">No quiz history yet. Start your first quiz!</p>
          </div>
        ) : (
          <div className="space-y-4">
            {history.map((item) => (
              <div
                key={item.attemptId}
                className="bg-slate-800/30 backdrop-blur-sm p-6 rounded-xl border border-purple-500/20 hover:border-purple-500/40 transition-all"
              >
                <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                  <div className="flex-1">
                    <div className="flex items-center gap-3 mb-2">
                      <h3 className="text-xl font-bold text-white capitalize">{item.category}</h3>
                      <span className={`px-2 py-1 rounded text-xs font-semibold ${getDifficultyColor(item.difficulty)}`}>
                        {item.difficulty}
                      </span>
                    </div>
                    <p className="text-sm text-gray-400 flex items-center">
                      <Clock className="w-4 h-4 mr-1" />
                      {new Date(item.completedAt).toLocaleDateString('en-US', {
                        year: 'numeric',
                        month: 'long',
                        day: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit'
                      })}
                    </p>
                  </div>
                  <div className="flex items-center gap-6">
                    <div className="text-center">
                      <div className={`text-3xl font-bold ${getScoreColor(item.percentage)}`}>
                        {item.percentage}%
                      </div>
                      <div className="text-xs text-gray-400">Score</div>
                    </div>
                    <div className="text-center">
                      <div className="text-2xl font-bold text-purple-400">{item.score}</div>
                      <div className="text-xs text-gray-400">Points</div>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        {history.length > 0 && (
          <div className="mt-8 bg-slate-800/30 backdrop-blur-sm p-6 rounded-xl border border-purple-500/20">
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-3">
                <TrendingUp className="w-8 h-8 text-green-400" />
                <div>
                  <h3 className="text-lg font-bold text-white">Keep it up!</h3>
                  <p className="text-sm text-gray-400">You've completed {history.length} quizzes</p>
                </div>
              </div>
              <div className="text-right">
                <div className="text-2xl font-bold text-purple-400">
                  {Math.round(history.reduce((acc, item) => acc + item.percentage, 0) / history.length)}%
                </div>
                <div className="text-xs text-gray-400">Average Score</div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default HistoryPage;