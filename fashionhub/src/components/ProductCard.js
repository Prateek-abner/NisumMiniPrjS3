import React from 'react';
import { useNavigate } from 'react-router-dom';

const ProductCard = ({ product }) => {
  const navigate = useNavigate();

  const handleViewDetails = () => {
    navigate(`/product/${product.productId}`);
  };

  const getStockStatus = () => {
    if (product.quantityInStock > 10) return 'In Stock';
    if (product.quantityInStock > 0) return `Low Stock (${product.quantityInStock} left)`;
    return 'Out of Stock';
  };

  const getStockClass = () => {
    if (product.quantityInStock > 10) return 'in-stock';
    if (product.quantityInStock > 0) return 'low-stock';
    return 'out-of-stock';
  };

  const getImageUrl = (imageUrl) => {
    if (!imageUrl) return '/placeholder-image.jpg';

    if (imageUrl.startsWith('http')) {
      return imageUrl;
    }

    return `http://localhost:8080/${imageUrl}`;
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(price).replace('₹', '₹');
  };

  return (
    <div className="product-card">
      <div className="product-image">
        <img
          src={getImageUrl(product.imageUrl)}
          alt={product.productName}
          onError={(e) => {
            e.target.src = '/placeholder-image.jpg';
          }}
        />
      </div>

      <div className="product-info">
        <h3>{product.productName}</h3>

        <div className="price-section">
          <span className="current-price">{formatPrice(product.price)}</span>
          {product.originalPrice && product.originalPrice > product.price && (
            <span className="original-price">{formatPrice(product.originalPrice)}</span>
          )}
          {product.discountPercent > 0 && (
            <span className="discount-badge">{product.discountPercent}% OFF</span>
          )}
        </div>

        <div className="product-meta">
          <span className="category">Category: {product.categoryName}</span>
          <span className={`stock-status ${getStockClass()}`}>
            {getStockStatus()}
          </span>
        </div>

        <div className="product-brand">
          Brand: {product.brand}
        </div>

        <div className="product-description">
          {product.description}
        </div>

        <button className="view-details-btn" onClick={handleViewDetails}>
          View Details
        </button>
      </div>
    </div>
  );
};

export default ProductCard;
