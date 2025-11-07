import { Crown} from 'lucide-react';


export function LeaderboardPage() {
  const mockLeaderboard = [
    { rank: 1, username: 'QuizMaster', score: 9500, avatar: '👑' },
    { rank: 2, username: 'BrainPower', score: 9200, avatar: '🧠' },
    { rank: 3, username: 'SmartUser', score: 8900, avatar: '⭐' },
    { rank: 4, username: 'ThinkFast', score: 8600, avatar: '🚀' },
    { rank: 5, username: 'QuizNinja', score: 8300, avatar: '🥷' }
  ];

  return (
    <div className="max-w-4xl mx-auto px-4 py-12">
      <h1 className="text-4xl font-bold text-white mb-8 text-center">Global Leaderboard</h1>

      <div className="space-y-4">
        {mockLeaderboard.map((player, idx) => (
          <div
            key={player.rank}
            className={`backdrop-blur-sm p-6 rounded-xl border transition-all ${
              idx === 0
                ? 'bg-gradient-to-r from-yellow-500/20 to-orange-500/20 border-yellow-500/50 shadow-lg shadow-yellow-500/20'
                : idx === 1
                ? 'bg-gradient-to-r from-gray-400/20 to-gray-500/20 border-gray-400/50'
                : idx === 2
                ? 'bg-gradient-to-r from-orange-700/20 to-orange-800/20 border-orange-700/50'
                : 'bg-slate-800/30 border-purple-500/20'
            }`}
          >
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-4">
                <div className="text-4xl">{player.avatar}</div>
                <div>
                  <div className="flex items-center space-x-2">
                    {idx < 3 && <Crown className={`w-5 h-5 ${idx === 0 ? 'text-yellow-400' : idx === 1 ? 'text-gray-400' : 'text-orange-700'}`} />}
                    <span className="text-xl font-bold text-white">{player.username}</span>
                  </div>
                  <div className="text-sm text-gray-400">Rank #{player.rank}</div>
                </div>
              </div>
              <div className="text-right">
                <div className="text-2xl font-bold text-purple-400">{player.score.toLocaleString()}</div>
                <div className="text-sm text-gray-400">Points</div>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}