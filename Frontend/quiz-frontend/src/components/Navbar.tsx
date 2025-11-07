import { useState } from 'react';
import {Trophy, LogOut, Menu, X,  Zap,  Home, History, User } from 'lucide-react';
import { useAuth } from '../hooks/useAuth';
import { useNavigate, useLocation } from 'react-router-dom';

export function Navbar() {
  const { user, logout } = useAuth();
  const [mobileOpen, setMobileOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const routeMap: Record<string, string> = {
    dashboard: '/dashboard',
    history: '/history',
    leaderboard: '/leaderboard',
    profile: '/profile',
  };

  const navItems = [
    { id: 'dashboard', label: 'Dashboard', icon: Home },
    { id: 'history', label: 'History', icon: History },
    { id: 'leaderboard', label: 'Leaderboard', icon: Trophy },
    { id: 'profile', label: 'Profile', icon: User }
  ];

  return (
    <nav className="fixed top-0 left-0 right-0 z-50 bg-slate-900/80 backdrop-blur-lg border-b border-purple-500/20">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center cursor-pointer" onClick={() => navigate(routeMap.dashboard)}>
            <Zap className="w-8 h-8 text-purple-400" />
            <span className="ml-2 text-2xl font-bold bg-gradient-to-r from-purple-400 to-pink-400 bg-clip-text text-transparent">
              Quiztacular
            </span>
          </div>

          {/* Desktop Nav */}
          <div className="hidden md:flex items-center space-x-1">
            {navItems.map(item => {
              const Icon = item.icon;
              const path = routeMap[item.id];
              const isActive = location.pathname === path || location.pathname.startsWith(path + '/');
              return (
                <button
                  key={item.id}
                  onClick={() => navigate(path)}
                  className={`px-4 py-2 rounded-lg transition-all flex items-center space-x-2 ${
                    isActive
                      ? 'bg-purple-500/20 text-purple-300'
                      : 'text-gray-300 hover:bg-slate-800/50'
                  }`}
                >
                  <Icon className="w-4 h-4" />
                  <span>{item.label}</span>
                </button>
              );
            })}
          </div>

          <div className="hidden md:flex items-center space-x-4">
            <div className="text-sm text-gray-300">
              <User className="w-4 h-4 inline mr-1" />
              {user?.username}
            </div>
            <button
              onClick={logout}
              className="px-4 py-2 bg-red-500/20 hover:bg-red-500/30 text-red-300 rounded-lg transition-all flex items-center space-x-2"
            >
              <LogOut className="w-4 h-4" />
              <span>Logout</span>
            </button>
          </div>

          {/* Mobile menu button */}
          <button
            onClick={() => setMobileOpen(!mobileOpen)}
            className="md:hidden p-2 rounded-lg bg-slate-800/50"
          >
            {mobileOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
          </button>
        </div>
      </div>

      {/* Mobile menu */}
      {mobileOpen && (
        <div className="md:hidden bg-slate-900 border-t border-purple-500/20">
          <div className="px-4 py-4 space-y-2">
            {navItems.map(item => {
              const Icon = item.icon;
              const path = routeMap[item.id];
              const isActive = location.pathname === path || location.pathname.startsWith(path + '/');
              return (
                <button
                  key={item.id}
                  onClick={() => {
                    navigate(path);
                    setMobileOpen(false);
                  }}
                  className={`w-full px-4 py-3 rounded-lg transition-all flex items-center space-x-3 ${
                    isActive
                      ? 'bg-purple-500/20 text-purple-300'
                      : 'text-gray-300 hover:bg-slate-800/50'
                  }`}
                >
                  <Icon className="w-5 h-5" />
                  <span>{item.label}</span>
                </button>
              );
            })}
            <button
              onClick={() => {
                logout();
                setMobileOpen(false);
              }}
              className="w-full px-4 py-3 bg-red-500/20 hover:bg-red-500/30 text-red-300 rounded-lg transition-all flex items-center space-x-3"
            >
              <LogOut className="w-5 h-5" />
              <span>Logout</span>
            </button>
          </div>
        </div>
      )}
    </nav>
  );
}