import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
  withCredentials: false, // Set to false to avoid CORS issues
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    console.log('🚀 API Request:', config.method?.toUpperCase(), config.url);
    return config;
  },
  (error) => {
    console.error('❌ API Request Error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor
api.interceptors.response.use(
  (response) => {
    console.log('✅ API Response:', response.status, response.config.url);
    return response;
  },
  (error) => {
    console.error('❌ API Response Error:', error.response?.status, error.config?.url);
    if (error.code === 'ECONNABORTED') {
      console.error('Request timeout - check if backend is running');
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  login: (credentials) => api.post('/auth/login', credentials),
  register: (userData) => api.post('/auth/register', userData),
  checkEmail: (email) => api.get(`/auth/check-email?email=${email}`),
  test: () => api.get('/auth/test'),
};

export const productAPI = {
  getAllProducts: () => api.get('/products'),
  getProductById: (id) => api.get(`/products/${id}`),
  getProductsByCategory: (categoryId) => api.get(`/products/category/${categoryId}`),
  searchProducts: (name) => api.get(`/products/search?name=${name}`),
  getProductsByBrand: (brand) => api.get(`/products/brand/${brand}`),
  getProductsByPriceRange: (minPrice, maxPrice) => api.get(`/products/price-range?minPrice=${minPrice}&maxPrice=${maxPrice}`),
  getDiscountedProducts: () => api.get('/products/discounted'),
  getAvailableProducts: () => api.get('/products/available'),
  getLatestProducts: () => api.get('/products/latest'),
  getAllBrands: () => api.get('/products/brands'),
  getAllSizes: () => api.get('/products/sizes'),
};

export const categoryAPI = {
  getAllCategories: () => api.get('/categories'),
  getCategoryById: (id) => api.get(`/categories/${id}`),
  searchCategories: (name) => api.get(`/categories/search?name=${name}`),
};

export default api;
