import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import CategoryCard from '../components/CategoryCard';
import { categoryAPI, productAPI } from '../services/api';

const Home = () => {
  const [categories, setCategories] = useState([]);
  const [categoryStats, setCategoryStats] = useState({});
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      await Promise.all([fetchCategories(), fetchCategoryStats()]);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchCategories = async () => {
    try {
      const response = await categoryAPI.getAllCategories();
      setCategories(response.data);
    } catch (error) {
      console.error('Error fetching categories:', error);
    }
  };

  const fetchCategoryStats = async () => {
    try {
      const response = await productAPI.getAllProducts();
      const products = response.data;

      const stats = {};
      products.forEach(product => {
        const categoryName = product.categoryName;
        if (!stats[categoryName]) {
          stats[categoryName] = { count: 0, totalDiscount: 0, products: 0 };
        }
        stats[categoryName].count++;
        stats[categoryName].totalDiscount += product.discountPercent || 0;
        stats[categoryName].products++;
      });

      Object.keys(stats).forEach(category => {
        stats[category].avgDiscount = Math.round(stats[category].totalDiscount / stats[category].products);
      });

      setCategoryStats(stats);
    } catch (error) {
      console.error('Error fetching category stats:', error);
    }
  };

  const handleCategoryClick = (categoryName) => {
    navigate(`/browse?category=${categoryName.toLowerCase()}`);
  };

  if (loading) {
    return <div className="loading">Loading amazing products...</div>;
  }

  return (
    <div className="home-page">
      <section className="hero-section">
        <div className="hero-content">
          <div className="hero-header">
            <div className="hero-text">
              <h1>Welcome to <span className="brand-name">FashionHub</span></h1>
              <p>Discover the latest trends in fashion with our curated collection of premium clothing and accessories. Experience style like never before.</p>
            </div>
            <div className="hero-logo">
              <div className="hero-logo-container">
                <div className="hero-logo-shape"></div>
                <div className="hero-logo-accent1"></div>
                <div className="hero-logo-accent2"></div>
                <div className="hero-logo-center"></div>
              </div>
            </div>
          </div>

          <div className="features">
            <div className="feature">
              <span className="feature-icon">⭐</span>
              <span>Premium Quality</span>
            </div>
            <div className="feature">
              <span className="feature-icon">🚚</span>
              <span>Free Delivery</span>
            </div>
            <div className="feature">
              <span className="feature-icon">↩️</span>
              <span>Easy Returns</span>
            </div>
          </div>

          <div className="hero-buttons">
            <button className="btn-primary" onClick={() => navigate('/browse')}>
              Shop Now →
            </button>
            <button className="btn-secondary" onClick={() => navigate('/browse')}>
              Explore Collections
            </button>
          </div>

          <div className="category-preview">
            <div className="category-btn men" onClick={() => handleCategoryClick('men')}>
              👔 Men's Fashion
            </div>
            <div className="category-btn women" onClick={() => handleCategoryClick('women')}>
              👗 Women's Style
            </div>
            <div className="category-btn kids" onClick={() => handleCategoryClick('kids')}>
              🧸 Kids Collection
            </div>
          </div>
        </div>
      </section>

      <section className="categories-section">
        <h2>Shop by Category</h2>
        <div className="categories-grid">
          {categories.map((category, index) => (
            <div key={category.categoryId} style={{animationDelay: `${index * 0.1}s`}}>
              <CategoryCard
                category={category}
                stats={categoryStats[category.categoryName] || { count: 0, avgDiscount: 0 }}
              />
            </div>
          ))}
        </div>
      </section>
    </div>
  );
};

export default Home;
