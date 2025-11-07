import { useState } from "react";
// import quizService from "../services/quizService";
import { CATEGORIES } from "../utils/constants";
import { useNavigate } from 'react-router-dom';

export function SoloQuizSetup() {
  const [selectedCategory, setSelectedCategory] = useState('');
  const [difficulty, setDifficulty] = useState('medium');
  const [questionCount, setQuestionCount] = useState(10);
  const navigate = useNavigate();

  const handleStart = () => {
    if (!selectedCategory) {
      alert('Please select a category');
      return;
    }
    navigate('/solo-quiz/play');
  };

  return (
    <div className="max-w-5xl mx-auto px-4 py-12">
      <button
        onClick={() => navigate('/dashboard')}
        className="mb-8 px-4 py-2 bg-slate-800/50 hover:bg-slate-800 text-gray-300 rounded-lg transition-all flex items-center space-x-2"
      >
        <span>←</span>
        <span>Back to Dashboard</span>
      </button>

      <h1 className="text-4xl font-bold text-white mb-8">Setup Your Quiz</h1>

      {/* Categories */}
      <div className="mb-10">
        <h2 className="text-xl font-semibold text-white mb-4">Select Category</h2>
        <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
          {CATEGORIES.map(cat => (
            <button
              key={cat.id}
              onClick={() => setSelectedCategory(cat.id)}
              className={`p-6 rounded-xl transition-all transform hover:scale-105 ${
                selectedCategory === cat.id
                  ? `bg-gradient-to-br ${cat.color} border-2 border-white shadow-lg`
                  : 'bg-slate-800/30 border border-purple-500/20 hover:bg-slate-800/50'
              }`}
            >
              <div className="text-4xl mb-2">{cat.icon}</div>
              <div className="text-lg font-semibold text-white">{cat.name}</div>
            </button>
          ))}
        </div>
      </div>

      {/* Difficulty */}
      <div className="mb-10">
        <h2 className="text-xl font-semibold text-white mb-4">Select Difficulty</h2>
        <div className="grid grid-cols-3 gap-4">
          {['easy', 'medium', 'hard'].map(diff => (
            <button
              key={diff}
              onClick={() => setDifficulty(diff)}
              className={`p-4 rounded-xl transition-all transform hover:scale-105 ${
                difficulty === diff
                  ? 'bg-gradient-to-r from-green-500 to-emerald-500 text-white shadow-lg'
                  : 'bg-slate-800/30 text-gray-300 border border-purple-500/20 hover:bg-slate-800/50'
              }`}
            >
              <div className="text-lg font-semibold capitalize">{diff}</div>
            </button>
          ))}
        </div>
      </div>

      {/* Question Count */}
      <div className="mb-10">
        <h2 className="text-xl font-semibold text-white mb-4">Number of Questions: {questionCount}</h2>
        <input
          type="range"
          min="5"
          max="20"
          step="5"
          value={questionCount}
          onChange={(e) => setQuestionCount(parseInt(e.target.value))}
          className="w-full h-2 bg-slate-700 rounded-lg appearance-none cursor-pointer accent-purple-500"
        />
        <div className="flex justify-between text-sm text-gray-400 mt-2">
          <span>5</span>
          <span>10</span>
          <span>15</span>
          <span>20</span>
        </div>
      </div>

      {/* Start Button */}
      <button
        onClick={handleStart}
        disabled={!selectedCategory}
        className="w-full py-4 bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 disabled:from-gray-500 disabled:to-gray-600 disabled:cursor-not-allowed text-white rounded-xl font-bold text-lg transition-all transform hover:scale-105 shadow-lg disabled:shadow-none"
      >
        Start Quiz
      </button>
    </div>
  );
}
