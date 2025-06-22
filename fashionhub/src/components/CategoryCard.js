import React from 'react';
import { useNavigate } from 'react-router-dom';

const CategoryCard = ({ category, stats }) => {
  const navigate = useNavigate();

  const handleExplore = () => {
    navigate(`/browse?category=${category.categoryName.toLowerCase()}`);
  };

  const getCategoryIcon = (categoryName) => {
    switch (categoryName.toLowerCase()) {
      case 'men': return '👔';
      case 'women': return '👗';
      case 'kids': return '🧸';
      default: return '🛍️';
    }
  };

  const getCategoryColor = (categoryName) => {
    switch (categoryName.toLowerCase()) {
      case 'men': return 'men-category';
      case 'women': return 'women-category';
      case 'kids': return 'kids-category';
      default: return 'default-category';
    }
  };

  return (
    <div className={`category-card ${getCategoryColor(category.categoryName)}`}>
      <div className="category-icon">
        {getCategoryIcon(category.categoryName)}
      </div>

      <h3>{category.categoryName}</h3>
      <p>{category.description}</p>

      <div className="category-stats">
        <div className="stat">
          <span className="stat-number">{stats.count || 0}</span>
          <span className="stat-label">Products</span>
        </div>
        <div className="stat">
          <span className="stat-number">{stats.avgDiscount || 0}%</span>
          <span className="stat-label">Discount</span>
        </div>
      </div>

      <button className="explore-btn" onClick={handleExplore}>
        Explore {category.categoryName}
      </button>
    </div>
  );
};

export default CategoryCard;
