import React, { createContext, useState, useContext, useEffect } from 'react';
import { authAPI } from '../services/api';

const AuthContext = createContext();

export const useAuth = () => {
  return useContext(AuthContext);
};

export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const login = async (credentials) => {
    try {
      console.log('Attempting login with:', credentials);

      const response = await authAPI.login(credentials);
      console.log('Login response:', response.data);

      if (response.data.success) {
        const userData = {
          userId: response.data.userId,
          firstName: response.data.firstName,
          lastName: response.data.lastName,
          email: response.data.email
        };

        localStorage.setItem('user', JSON.stringify(userData));
        setCurrentUser(userData);

        return response.data;
      } else {
        throw new Error(response.data.message);
      }
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  };

  const register = async (userData) => {
    try {
      console.log('Attempting registration with:', userData);

      const response = await authAPI.register(userData);
      console.log('Registration response:', response.data);

      return response.data;
    } catch (error) {
      console.error('Registration error:', error);
      throw error;
    }
  };

  const checkEmailAvailability = async (email) => {
    try {
      const response = await authAPI.checkEmail(email);
      return response.data;
    } catch (error) {
      console.error('Email check error:', error);
      throw error;
    }
  };

  const logout = () => {
    localStorage.removeItem('user');
    setCurrentUser(null);
  };

  useEffect(() => {
    const user = localStorage.getItem('user');

    if (user) {
      try {
        setCurrentUser(JSON.parse(user));
      } catch (error) {
        console.error('Error parsing user data:', error);
        localStorage.removeItem('user');
      }
    }
    setLoading(false);
  }, []);

  const value = {
    currentUser,
    login,
    register,
    checkEmailAvailability,
    logout,
  };

  return (
    <AuthContext.Provider value={value}>
      {!loading && children}
    </AuthContext.Provider>
  );
};
