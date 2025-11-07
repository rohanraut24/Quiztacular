import React, { useState, useEffect } from 'react';
import { Users } from 'lucide-react';
import quizService from '../services/quizService';
import type { Room, Category } from '../types';
import { useNavigate } from 'react-router-dom';

export function MultiplayerBrowser() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [rooms] = useState<Room[]>([
    {
      id: 1,
      roomCode: 'ABC123',
      roomName: 'Tech Masters',
      category: 'computers',
      currentPlayers: 3,
      maxPlayers: 10,
      status: 'WAITING',
      difficulty: 'medium',
      totalQuestions: 0,
    },
    {
      id: 2,
      roomCode: 'XYZ789',
      roomName: 'Science Quiz',
      category: 'science',
      currentPlayers: 5,
      maxPlayers: 8,
      status: 'WAITING',
      difficulty: 'hard',
      totalQuestions: 0,
    },
    {
      id: 3,
      roomCode: 'DEF456',
      roomName: 'Math Challenge',
      category: 'mathematics',
      currentPlayers: 2,
      maxPlayers: 6,
      status: 'WAITING',
      difficulty: 'easy',
      totalQuestions: 0,
    },
  ]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await quizService.getCategories();
        // Ensure you access array correctly (depends on API response)
        setCategories(response.categories || response);
      } catch (error) {
        console.error('Failed to fetch categories:', error);
      }
    };

    fetchCategories();
  }, []);
  const navigate = useNavigate();

  return (
    <div className="max-w-6xl mx-auto px-4 py-12">
      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-4xl font-bold text-white mb-2">Multiplayer Rooms</h1>
          <p className="text-gray-400">Join a room or create your own</p>
        </div>
        <button
          onClick={() => navigate('/create-room')}
          className="px-6 py-3 bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white rounded-xl font-semibold transition-all transform hover:scale-105 shadow-lg"
        >
          Create Room
        </button>
      </div>

      <div className="grid gap-6">
        {rooms.map((room) => {
          const category = categories.find((c) => c.id === room.category);

          return (
            <div
              key={room.id}
              className="bg-slate-800/30 backdrop-blur-sm p-6 rounded-xl border border-purple-500/20 hover:border-purple-500/40 transition-all"
            >
              <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div className="flex items-center space-x-4">
                  <div
                    className={`w-16 h-16 rounded-xl bg-gradient-to-br ${
                      category?.color || 'from-gray-500 to-gray-700'
                    } flex items-center justify-center text-3xl`}
                  >
                    {category?.icon || '❓'}
                  </div>
                  <div>
                    <h3 className="text-xl font-bold text-white mb-1">{room.roomName}</h3>
                    <div className="flex items-center space-x-4 text-sm text-gray-400">
                      <span className="flex items-center">
                        <Users className="w-4 h-4 mr-1" />
                        {room.currentPlayers}/{room.maxPlayers}
                      </span>
                      <span className="px-2 py-1 bg-yellow-500/20 text-yellow-400 rounded">
                        {room.difficulty}
                      </span>
                      <span className="px-2 py-1 bg-green-500/20 text-green-400 rounded">
                        {room.status}
                      </span>
                    </div>
                    <div className="text-sm text-purple-400 mt-1 font-mono">
                      Code: {room.roomCode}
                    </div>
                  </div>
                </div>
                <button className="px-6 py-3 bg-purple-500/20 hover:bg-purple-500/30 text-purple-300 rounded-lg font-semibold transition-all">
                  Join Room
                </button>
              </div>
            </div>
          );
        })}
      </div>

      {rooms.length === 0 && (
        <div className="text-center py-16">
          <Users className="w-16 h-16 text-gray-600 mx-auto mb-4" />
          <p className="text-gray-400 text-lg">No active rooms. Be the first to create one!</p>
        </div>
      )}
    </div>
  );
}
