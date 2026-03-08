import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import PortfolioDetailPage from './pages/PortfolioDetailPage';
import AddAssetPage from './pages/AddAssetPage';

function App() {
    const isAuthenticated = !!localStorage.getItem('token');

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/dashboard" element={
                    isAuthenticated ? <DashboardPage /> : <Navigate to="/login" />
                } />
                <Route path="/portfolio/:id" element={
                    isAuthenticated ? <PortfolioDetailPage /> : <Navigate to="/login" />
                } />
                <Route path="/portfolio/:id/add-asset" element={
                    isAuthenticated ? <AddAssetPage /> : <Navigate to="/login" />
                } />
                <Route path="*" element={<Navigate to="/login" />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
