import quizService from "../services/quizService";
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import type { Category } from '../types';

export function CreateRoom() {
  const [roomName, setRoomName] = useState('');
  const [category, setCategory] = useState('');
  const [difficulty, setDifficulty] = useState('medium');
  const [maxPlayers, setMaxPlayers] = useState(5);
  const [questionCount, setQuestionCount] = useState(10);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loadingCategories, setLoadingCategories] = useState(false);
  const navigate = useNavigate();

  const handleCreate = () => {
    if (!roomName || !category) {
      alert('Please fill all fields');
      return;
    }
    alert('Room created! (Mock - WebSocket integration needed)');
    navigate('/multiplayer');
  };

  useEffect(() => {
    let mounted = true;
    const fetch = async () => {
      setLoadingCategories(true);
      try {
        const res = await quizService.getCategories();
        if (!mounted) return;
        setCategories(res.categories || []);
      } catch (err) {
        console.error('Failed to load categories', err);
      } finally {
        if (mounted) setLoadingCategories(false);
      }
    };
    fetch();
    return () => { mounted = false; };
  }, []);

  return (
    <div className="max-w-4xl mx-auto px-4 py-12">
      <button
        onClick={() => navigate('/multiplayer')}
        className="mb-8 px-4 py-2 bg-slate-800/50 hover:bg-slate-800 text-gray-300 rounded-lg transition-all flex items-center space-x-2"
      >
        <span>←</span>
        <span>Back</span>
      </button>

      <h1 className="text-4xl font-bold text-white mb-8">Create Your Room</h1>

      <div className="bg-slate-800/30 backdrop-blur-sm p-8 rounded-2xl border border-purple-500/20">
        <div className="mb-6">
          <label className="block text-sm font-medium text-gray-300 mb-2">Room Name</label>
          <input
            type="text"
            value={roomName}
            onChange={(e) => setRoomName(e.target.value)}
            className="w-full px-4 py-3 bg-slate-900/50 border border-purple-500/30 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:border-purple-500"
            placeholder="Enter room name"
          />
        </div>

        <div className="mb-6">
          <label className="block text-sm font-medium text-gray-300 mb-3">Category</label>
          <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
            {loadingCategories ? (
              <div className="text-gray-400">Loading categories...</div>
            ) : (
              categories.map(cat => (
              <button
                key={cat.id}
                onClick={() => setCategory(cat.id)}
                className={`p-4 rounded-lg transition-all ${
                  category === cat.id
                    ? `bg-gradient-to-br ${cat.color} shadow-lg`
                    : 'bg-slate-700/30 hover:bg-slate-700/50 border border-purple-500/20'
                }`}
              >
                <div className="text-2xl mb-1">{cat.icon}</div>
                <div className="text-sm font-medium text-white">{cat.name}</div>
              </button>
              ))
            )}
          </div>
        </div>

        <div className="mb-6">
          <label className="block text-sm font-medium text-gray-300 mb-3">Difficulty</label>
          <div className="grid grid-cols-3 gap-3">
            {['easy', 'medium', 'hard'].map(diff => (
              <button
                key={diff}
                onClick={() => setDifficulty(diff)}
                className={`p-3 rounded-lg transition-all ${
                  difficulty === diff
                    ? 'bg-gradient-to-r from-green-500 to-emerald-500 text-white'
                    : 'bg-slate-700/30 text-gray-300 border border-purple-500/20'
                }`}
              >
                <div className="capitalize font-medium">{diff}</div>
              </button>
            ))}
          </div>
        </div>

        <div className="mb-6">
          <label className="block text-sm font-medium text-gray-300 mb-2">
            Max Players: {maxPlayers}
          </label>
          <input
            type="range"
            min="2"
            max="10"
            value={maxPlayers}
            onChange={(e) => setMaxPlayers(parseInt(e.target.value))}
            className="w-full h-2 bg-slate-700 rounded-lg appearance-none cursor-pointer accent-purple-500"
          />
        </div>

        <div className="mb-8">
          <label className="block text-sm font-medium text-gray-300 mb-2">
            Questions: {questionCount}
          </label>
          <input
            type="range"
            min="5"
            max="20"
            step="5"
            value={questionCount}
            onChange={(e) => setQuestionCount(parseInt(e.target.value))}
            className="w-full h-2 bg-slate-700 rounded-lg appearance-none cursor-pointer accent-purple-500"
          />
        </div>

        <button
          onClick={handleCreate}
          className="w-full py-4 bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white rounded-xl font-bold text-lg transition-all shadow-lg"
        >
          Create Room
        </button>
      </div>
    </div>
  );
}