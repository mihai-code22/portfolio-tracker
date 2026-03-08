import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api/v1',
});

// adauga automat JWT token la fiecare request
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;

// Auth
export const login = (username, password) =>
    api.post('/auth/login', { username, password });

export const register = (username, email, password) =>
    api.post('/auth/register', { username, email, password });

// Portfolios
export const getMyPortfolios = () =>
    api.get('/portfolios/me');

export const getPortfolioPnl = () =>
    api.get('/portfolios/me/pnl');

export const getPortfolioById = (portfolioId) =>
    api.get(`/portfolios/${portfolioId}`);

export const createPortfolio = (name) =>
    api.post('/portfolios/me', { name });

export const deletePortfolio = (portfolioId) =>
    api.delete(`/portfolios/${portfolioId}`);

// Assets
export const getAssetsByPortfolioId = (portfolioId) =>
    api.get(`/assets/portfolio/${portfolioId}`);

export const getAssetsPnl = (portfolioId) =>
    api.get(`/assets/portfolio/${portfolioId}/pnl`);

// Prices
export const getPriceHistory = (symbol) =>
    api.get(`/prices/${symbol}/history`);

export const createAsset = (portfolioId, payload) =>
    api.post(`/assets/portfolio/${portfolioId}`, payload);

export const deleteAsset = (assetId) =>
    api.delete(`/assets/${assetId}`);