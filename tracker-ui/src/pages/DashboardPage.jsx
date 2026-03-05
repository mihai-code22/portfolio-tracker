import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getMyPortfolios, createPortfolio, deletePortfolio } from '../services/api';

function getUserIdFromToken() {
    try {
        const token = localStorage.getItem('token');
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.userId ?? payload.sub ?? null;
    } catch {
        return null;
    }
}

const styles = {
    page: {
        minHeight: '100vh',
        background: 'linear-gradient(135deg, #0f0f1a 0%, #1a1a2e 50%, #16213e 100%)',
        fontFamily: "'Segoe UI', system-ui, -apple-system, sans-serif",
        color: '#ffffff',
    },
    navbar: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        padding: '0 32px',
        height: '60px',
        borderBottom: '1px solid rgba(255,255,255,0.07)',
        background: 'rgba(0,0,0,0.2)',
        backdropFilter: 'blur(12px)',
        position: 'sticky',
        top: 0,
        zIndex: 10,
    },
    brand: {
        fontWeight: '700',
        fontSize: '18px',
        letterSpacing: '-0.3px',
        background: 'linear-gradient(135deg, #4a9eff, #a78bfa)',
        WebkitBackgroundClip: 'text',
        WebkitTextFillColor: 'transparent',
    },
    logoutBtn: {
        background: 'rgba(255,255,255,0.06)',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '8px',
        color: 'rgba(255,255,255,0.65)',
        padding: '7px 16px',
        fontSize: '13px',
        fontWeight: '500',
        cursor: 'pointer',
        transition: 'background 0.2s, color 0.2s',
    },
    main: {
        maxWidth: '960px',
        margin: '0 auto',
        padding: '40px 24px',
    },
    heading: {
        fontSize: '22px',
        fontWeight: '700',
        margin: '0 0 6px 0',
        letterSpacing: '-0.4px',
    },
    subheading: {
        color: 'rgba(255,255,255,0.35)',
        fontSize: '14px',
        margin: '0 0 28px 0',
    },
    createForm: {
        display: 'flex',
        gap: '10px',
        marginBottom: '36px',
    },
    input: {
        flex: 1,
        padding: '11px 14px',
        background: 'rgba(255,255,255,0.06)',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '8px',
        color: '#ffffff',
        fontSize: '14px',
        outline: 'none',
        boxSizing: 'border-box',
        transition: 'border-color 0.2s, background 0.2s',
    },
    inputFocus: {
        borderColor: 'rgba(99,179,237,0.55)',
        background: 'rgba(255,255,255,0.09)',
    },
    createBtn: {
        padding: '11px 20px',
        background: 'linear-gradient(135deg, #4a9eff 0%, #7b5ea7 100%)',
        border: 'none',
        borderRadius: '8px',
        color: '#ffffff',
        fontSize: '14px',
        fontWeight: '600',
        cursor: 'pointer',
        whiteSpace: 'nowrap',
        transition: 'opacity 0.2s',
    },
    error: {
        background: 'rgba(239,68,68,0.1)',
        border: '1px solid rgba(239,68,68,0.25)',
        borderRadius: '8px',
        color: '#fc8181',
        fontSize: '13px',
        padding: '10px 14px',
        marginBottom: '20px',
        lineHeight: '1.5',
    },
    grid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))',
        gap: '16px',
    },
    card: {
        background: 'rgba(255,255,255,0.04)',
        border: '1px solid rgba(255,255,255,0.08)',
        borderRadius: '12px',
        padding: '22px',
        cursor: 'pointer',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'space-between',
        gap: '20px',
        transition: 'border-color 0.2s, background 0.2s, transform 0.15s',
        minHeight: '110px',
    },
    cardHover: {
        borderColor: 'rgba(74,158,255,0.35)',
        background: 'rgba(255,255,255,0.07)',
        transform: 'translateY(-2px)',
    },
    cardName: {
        fontWeight: '600',
        fontSize: '16px',
        color: '#ffffff',
        wordBreak: 'break-word',
    },
    cardFooter: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
    },
    viewHint: {
        fontSize: '12px',
        color: 'rgba(255,255,255,0.3)',
    },
    deleteBtn: {
        background: 'rgba(239,68,68,0.1)',
        border: '1px solid rgba(239,68,68,0.2)',
        borderRadius: '6px',
        color: '#fc8181',
        padding: '5px 12px',
        fontSize: '12px',
        fontWeight: '500',
        cursor: 'pointer',
        transition: 'background 0.2s',
    },
    empty: {
        color: 'rgba(255,255,255,0.25)',
        fontSize: '15px',
        textAlign: 'center',
        padding: '56px 0',
        gridColumn: '1 / -1',
    },
    loadingText: {
        color: 'rgba(255,255,255,0.35)',
        fontSize: '14px',
        textAlign: 'center',
        padding: '48px 0',
    },
};

function PortfolioCard({ portfolio, onDelete, onClick }) {
    const [hovered, setHovered] = useState(false);
    const [deleting, setDeleting] = useState(false);

    const handleDelete = async (e) => {
        e.stopPropagation();
        setDeleting(true);
        await onDelete(portfolio.id);
        setDeleting(false);
    };

    return (
        <div
            style={{ ...styles.card, ...(hovered ? styles.cardHover : {}) }}
            onClick={onClick}
            onMouseEnter={() => setHovered(true)}
            onMouseLeave={() => setHovered(false)}
        >
            <span style={styles.cardName}>{portfolio.name}</span>
            <div style={styles.cardFooter}>
                <span style={styles.viewHint}>View details →</span>
                <button
                    style={{
                        ...styles.deleteBtn,
                        opacity: deleting ? 0.5 : 1,
                        cursor: deleting ? 'not-allowed' : 'pointer',
                    }}
                    onClick={handleDelete}
                    disabled={deleting}
                >
                    {deleting ? '...' : 'Delete'}
                </button>
            </div>
        </div>
    );
}

export default function DashboardPage() {
    const navigate = useNavigate();
    const userId = getUserIdFromToken();

    const [portfolios, setPortfolios] = useState([]);
    const [newName, setNewName] = useState('');
    const [inputFocused, setInputFocused] = useState(false);
    const [loading, setLoading] = useState(true);
    const [creating, setCreating] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        if (!userId) {
            localStorage.removeItem('token');
            window.location.href = '/login';
            return;
        }
        fetchPortfolios();
    }, [userId]);

    const fetchPortfolios = async () => {
        try {
            const res = await getMyPortfolios();
            setPortfolios(res.data);
        } catch (err) {
            setError('Failed to load portfolios.');
        } finally {
            setLoading(false);
        }
    };

    const handleCreate = async (e) => {
        e.preventDefault();
        const name = newName.trim();
        if (!name) return;
        setCreating(true);
        setError('');
        try {
            const res = await createPortfolio(name);
            setPortfolios((prev) => [...prev, res.data]);
            setNewName('');
        } catch (err) {
            const msg = err?.response?.data?.message || err?.response?.data || err?.message;
            setError(typeof msg === 'string' ? msg : 'Failed to create portfolio.');
        } finally {
            setCreating(false);
        }
    };

    const handleDelete = async (portfolioId) => {
        setError('');
        try {
            await deletePortfolio(portfolioId);
            setPortfolios((prev) => prev.filter((p) => p.id !== portfolioId));
        } catch (err) {
            setError('Failed to delete portfolio.');
        }
    };

    const handleLogout = () => {
        localStorage.clear();
        window.location.href = '/login';
    };

    return (
        <div style={styles.page}>
            <nav style={styles.navbar}>
                <span style={styles.brand}>Portfolio Tracker</span>
                <button
                    style={styles.logoutBtn}
                    onClick={handleLogout}
                    onMouseEnter={(e) => {
                        e.currentTarget.style.background = 'rgba(255,255,255,0.1)';
                        e.currentTarget.style.color = '#ffffff';
                    }}
                    onMouseLeave={(e) => {
                        e.currentTarget.style.background = 'rgba(255,255,255,0.06)';
                        e.currentTarget.style.color = 'rgba(255,255,255,0.65)';
                    }}
                >
                    Log out
                </button>
            </nav>

            <main style={styles.main}>
                <h2 style={styles.heading}>Your Portfolios</h2>
                <p style={styles.subheading}>Manage and track your investment portfolios.</p>

                <form onSubmit={handleCreate} style={styles.createForm}>
                    <input
                        type="text"
                        value={newName}
                        onChange={(e) => setNewName(e.target.value)}
                        placeholder="New portfolio name…"
                        style={{
                            ...styles.input,
                            ...(inputFocused ? styles.inputFocus : {}),
                        }}
                        onFocus={() => setInputFocused(true)}
                        onBlur={() => setInputFocused(false)}
                    />
                    <button
                        type="submit"
                        disabled={creating || !newName.trim()}
                        style={{
                            ...styles.createBtn,
                            opacity: creating || !newName.trim() ? 0.55 : 1,
                            cursor: creating || !newName.trim() ? 'not-allowed' : 'pointer',
                        }}
                    >
                        {creating ? 'Creating…' : '+ New Portfolio'}
                    </button>
                </form>

                {error && <div style={styles.error}>{error}</div>}

                {loading ? (
                    <p style={styles.loadingText}>Loading portfolios…</p>
                ) : (
                    <div style={styles.grid}>
                        {portfolios.length === 0 ? (
                            <p style={styles.empty}>No portfolios yet. Create one above.</p>
                        ) : (
                            portfolios.map((p) => (
                                <PortfolioCard
                                    key={p.id}
                                    portfolio={p}
                                    onDelete={handleDelete}
                                    onClick={() => navigate(`/portfolio/${p.id}`)}
                                />
                            ))
                        )}
                    </div>
                )}
            </main>
        </div>
    );
}
