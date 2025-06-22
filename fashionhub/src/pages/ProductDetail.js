import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { productAPI } from '../services/api';

const ProductDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [product, setProduct] = useState(null);
  const [selectedSize, setSelectedSize] = useState('');
  const [quantity, setQuantity] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchProduct();
  }, [id]);

  const fetchProduct = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await productAPI.getProductById(id);
      setProduct(response.data);

      if (response.data.sizes && response.data.sizes.length > 0) {
        setSelectedSize(response.data.sizes[0]);
      }
    } catch (error) {
      console.error('Error fetching product:', error);
      setError('Failed to load product details');
    } finally {
      setLoading(false);
    }
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

  const handleAddToCart = () => {
    console.log('Added to cart:', {
      product: product.productId,
      size: selectedSize,
      quantity: quantity
    });
    alert('Product added to cart!');
  };

  const handleGoBack = () => {
    navigate(-1);
  };

  if (loading) {
    return <div className="loading">Loading product details...</div>;
  }

  if (error) {
    return (
      <div className="error-container">
        <h2>Error</h2>
        <p>{error}</p>
        <button onClick={handleGoBack} className="btn-secondary">
          Go Back
        </button>
      </div>
    );
  }

  if (!product) {
    return (
      <div className="error-container">
        <h2>Product Not Found</h2>
        <p>The product you're looking for doesn't exist.</p>
        <button onClick={handleGoBack} className="btn-secondary">
          Go Back
        </button>
      </div>
    );
  }

  return (
    <div className="product-detail">
      <div className="product-detail-container">
        <div className="product-image">
          <img
            src={getImageUrl(product.imageUrl)}
            alt={product.productName}
            onError={(e) => {
              console.error('Image failed to load:', e.target.src);
              e.target.src = '/placeholder-image.jpg';
            }}
          />
        </div>

        <div className="product-info">
          <button onClick={handleGoBack} className="back-btn">
            ← Back to Products
          </button>

          <h1>{product.productName}</h1>

          <div className="price-section">
            <span className="current-price">{formatPrice(product.price)}</span>
            {product.originalPrice && product.originalPrice > product.price && (
              <>
                <span className="original-price">{formatPrice(product.originalPrice)}</span>
                <span className="discount">({product.discountPercent}% OFF)</span>
              </>
            )}
          </div>

          <div className="offers-section">
            <h3>Available Offers</h3>
            {product.offers && product.offers.length > 0 ? (
              product.offers.map((offer, index) => (
                <div key={index} className="offer-item">• {offer}</div>
              ))
            ) : (
              <div className="offer-item">• 15% off on first purchase</div>
            )}
          </div>

          {product.sizes && product.sizes.length > 0 && (
            <div className="size-section">
              <h3>Size</h3>
              <div className="size-options">
                {product.sizes.map(size => (
                  <button
                    key={size}
                    className={`size-btn ${selectedSize === size ? 'selected' : ''}`}
                    onClick={() => setSelectedSize(size)}
                  >
                    {size}
                  </button>
                ))}
              </div>
            </div>
          )}

          <div className="quantity-section">
            <h3>Quantity</h3>
            <select
              value={quantity}
              onChange={(e) => setQuantity(parseInt(e.target.value))}
              className="quantity-select"
            >
              {[...Array(Math.min(10, product.quantityInStock))].map((_, i) => (
                <option key={i + 1} value={i + 1}>{i + 1}</option>
              ))}
            </select>
          </div>

          <div className="stock-info">
            {product.quantityInStock > 0 ? (
              <span className="in-stock">
                Only {product.quantityInStock} items left in stock - order soon!
              </span>
            ) : (
              <span className="out-of-stock">Out of Stock</span>
            )}
          </div>

          <button
            className="add-to-cart-btn"
            onClick={handleAddToCart}
            disabled={product.quantityInStock === 0}
          >
            {product.quantityInStock > 0 ? 'Add to Cart' : 'Out of Stock'}
          </button>

          <div className="product-details">
            <h3>Product Details</h3>
            <p><strong>Brand:</strong> {product.brand}</p>
            <p><strong>Category:</strong> {product.categoryName}</p>
            <p><strong>Description:</strong> {product.description}</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetail;
