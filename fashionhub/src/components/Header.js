import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Header = () => {
  const navigate = useNavigate();
  const { currentUser, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <header className="header">
      <div className="header-container">
        <Link to="/" className="logo">
          <div className="logo-icon">
            <div className="logo-shape"></div>
            <div className="logo-dot"></div>
          </div>
          <span className="logo-text">FashionHub</span>
        </Link>
        <nav className="nav-menu">
          <Link to="/" className="nav-link">Home</Link>
          <Link to="/browse" className="nav-link">Browse</Link>
          {currentUser ? (
            <>
              <span className="nav-link">Welcome, {currentUser.firstName}!</span>
              <button className="nav-button" onClick={handleLogout}>Logout</button>
            </>
          ) : (
            <>
              <Link to="/login" className="nav-button">Login</Link>
              <Link to="/register" className="nav-button register">Register</Link>
            </>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;
