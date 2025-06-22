import React, { useState, useEffect } from 'react';
import ProductCard from '../components/ProductCard';
import { productAPI, categoryAPI } from '../services/api';

const Browse = () => {
  const [products, setProducts] = useState([]);
  const [allProducts, setAllProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [brands, setBrands] = useState([]);
  const [filters, setFilters] = useState({
    category: 'All Categories',
    brand: 'All Brands',
    size: 'All Sizes',
    priceRange: 'All Prices',
    searchTerm: '',
    productId: ''
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchInitialData();
  }, []);

  const fetchInitialData = async () => {
    try {
      setLoading(true);
      setError(null);

      const [productsResponse, categoriesResponse] = await Promise.all([
        productAPI.getAllProducts().catch(err => {
          console.error('Products API error:', err);
          return { data: [] };
        }),
        categoryAPI.getAllCategories().catch(err => {
          console.error('Categories API error:', err);
          return { data: [] };
        })
      ]);

      const productsData = productsResponse.data || [];
      const categoriesData = categoriesResponse.data || [];

      setAllProducts(productsData);
      setProducts(productsData);
      setCategories(categoriesData);

      // Extract unique brands from products
      const uniqueBrands = [...new Set(productsData.map(product => product.brand).filter(Boolean))];
      setBrands(uniqueBrands);

    } catch (error) {
      console.error('Error fetching initial data:', error);
      setError('Failed to load products. Please refresh the page.');
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (filterType, value) => {
    setFilters(prev => ({
      ...prev,
      [filterType]: value
    }));
  };

  const applyFilters = () => {
    let filteredProducts = [...allProducts];

    // Search by product ID
    if (filters.productId.trim()) {
      filteredProducts = filteredProducts.filter(product =>
        product.productId?.toLowerCase().includes(filters.productId.toLowerCase())
      );
    }
    // Search by name/description
    else if (filters.searchTerm.trim()) {
      filteredProducts = filteredProducts.filter(product =>
        product.productName?.toLowerCase().includes(filters.searchTerm.toLowerCase()) ||
        product.description?.toLowerCase().includes(filters.searchTerm.toLowerCase())
      );
    }

    // Filter by category
    if (filters.category !== 'All Categories') {
      filteredProducts = filteredProducts.filter(product =>
        product.categoryName?.toLowerCase() === filters.category.toLowerCase()
      );
    }

    // Filter by brand
    if (filters.brand !== 'All Brands') {
      filteredProducts = filteredProducts.filter(product =>
        product.brand?.toLowerCase() === filters.brand.toLowerCase()
      );
    }

    // Filter by price range
    if (filters.priceRange !== 'All Prices') {
      const [min, max] = filters.priceRange.split('-').map(Number);
      filteredProducts = filteredProducts.filter(product => {
        const price = Number(product.price);
        if (max) {
          return price >= min && price <= max;
        } else {
          return price >= min; // For "5000+" case
        }
      });
    }

    setProducts(filteredProducts);
  };

  const handleSearch = () => {
    applyFilters();
  };

  const clearAllFilters = () => {
    setFilters({
      category: 'All Categories',
      brand: 'All Brands',
      size: 'All Sizes',
      priceRange: 'All Prices',
      searchTerm: '',
      productId: ''
    });
    setProducts(allProducts);
  };

  // Loading state
  if (loading) {
    return (
      <div className="browse-page">
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <p>Loading amazing products...</p>
        </div>
      </div>
    );
  }

  // Error state
  if (error) {
    return (
      <div className="browse-page">
        <div className="error-container">
          <h2>Oops! Something went wrong</h2>
          <p>{error}</p>
          <button onClick={fetchInitialData} className="btn-primary">
            Try Again
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="browse-page">
      <div className="search-section">
        <h1>Search & Browse Products</h1>

        {/* Search Inputs */}
        <div className="search-form">
          <div className="search-inputs">
            <input
              type="text"
              placeholder="Search by product name or description..."
              value={filters.searchTerm}
              onChange={(e) => handleFilterChange('searchTerm', e.target.value)}
              className="search-input"
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
            />
            <input
              type="text"
              placeholder="Enter product ID"
              value={filters.productId}
              onChange={(e) => handleFilterChange('productId', e.target.value)}
              className="search-input"
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
            />
          </div>
          <button onClick={handleSearch} className="search-btn">
            Search
          </button>
        </div>

        {/* Filter Dropdowns */}
        <div className="filters-section">
          <div className="filter-group">
            <label>Category:</label>
            <select
              value={filters.category}
              onChange={(e) => handleFilterChange('category', e.target.value)}
              className="filter-select"
            >
              <option value="All Categories">All Categories</option>
              {categories.map(category => (
                <option key={category.categoryId} value={category.categoryName}>
                  {category.categoryName}
                </option>
              ))}
            </select>
          </div>

          <div className="filter-group">
            <label>Brand:</label>
            <select
              value={filters.brand}
              onChange={(e) => handleFilterChange('brand', e.target.value)}
              className="filter-select"
            >
              <option value="All Brands">All Brands</option>
              {brands.map(brand => (
                <option key={brand} value={brand}>
                  {brand}
                </option>
              ))}
            </select>
          </div>

          <div className="filter-group">
            <label>Size:</label>
            <select
              value={filters.size}
              onChange={(e) => handleFilterChange('size', e.target.value)}
              className="filter-select"
            >
              <option value="All Sizes">All Sizes</option>
              <option value="S">S</option>
              <option value="M">M</option>
              <option value="L">L</option>
              <option value="XL">XL</option>
            </select>
          </div>

          <div className="filter-group">
            <label>Price Range:</label>
            <select
              value={filters.priceRange}
              onChange={(e) => handleFilterChange('priceRange', e.target.value)}
              className="filter-select"
            >
              <option value="All Prices">All Prices</option>
              <option value="0-500">₹0 - ₹500</option>
              <option value="500-1000">₹500 - ₹1000</option>
              <option value="1000-2000">₹1000 - ₹2000</option>
              <option value="2000-5000">₹2000 - ₹5000</option>
              <option value="5000">₹5000+</option>
            </select>
          </div>

          <button className="clear-filters-btn" onClick={clearAllFilters}>
            Clear All Filters
          </button>
        </div>

        {/* Results Count */}
        <div className="results-info">
          <p className="results-count">
            Showing {products.length} of {allProducts.length} products
          </p>
        </div>
      </div>

      {/* Products Grid */}
      <div className="products-container">
        {products.length > 0 ? (
          <div className="products-grid">
            {products.map((product, index) => (
              <div
                key={product.productId || index}
                className="product-item"
                style={{animationDelay: `${index * 0.1}s`}}
              >
                <ProductCard product={product} />
              </div>
            ))}
          </div>
        ) : (
          <div className="no-products">
            <div className="no-products-icon">🔍</div>
            <h3>No products found</h3>
            <p>Try adjusting your search criteria or filters.</p>
            <button className="btn-primary" onClick={clearAllFilters}>
              Clear All Filters
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default Browse;
