import { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getPortfolioById, getAssetsPnl, deleteAsset, getPriceHistory } from '../services/api';
import { usePnlWebSocket } from '../hooks/usePnlWebSocket';
import {
    LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
} from 'recharts';

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
        gap: '16px',
        padding: '0 32px',
        height: '60px',
        borderBottom: '1px solid rgba(255,255,255,0.07)',
        background: 'rgba(0,0,0,0.2)',
        backdropFilter: 'blur(12px)',
        position: 'sticky',
        top: 0,
        zIndex: 10,
    },
    backBtn: {
        background: 'rgba(255,255,255,0.06)',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '8px',
        color: 'rgba(255,255,255,0.65)',
        padding: '7px 14px',
        fontSize: '13px',
        fontWeight: '500',
        cursor: 'pointer',
        transition: 'background 0.2s, color 0.2s',
        display: 'flex',
        alignItems: 'center',
        gap: '6px',
    },
    brand: {
        fontWeight: '700',
        fontSize: '18px',
        letterSpacing: '-0.3px',
        background: 'linear-gradient(135deg, #4a9eff, #a78bfa)',
        WebkitBackgroundClip: 'text',
        WebkitTextFillColor: 'transparent',
    },
    main: {
        maxWidth: '960px',
        margin: '0 auto',
        padding: '40px 24px',
    },
    pageHeader: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '28px',
    },
    heading: {
        fontSize: '22px',
        fontWeight: '700',
        margin: 0,
        letterSpacing: '-0.4px',
    },
    addBtn: {
        padding: '10px 20px',
        background: 'linear-gradient(135deg, #4a9eff 0%, #7b5ea7 100%)',
        border: 'none',
        borderRadius: '8px',
        color: '#ffffff',
        fontSize: '14px',
        fontWeight: '600',
        cursor: 'pointer',
        transition: 'opacity 0.2s',
        letterSpacing: '0.2px',
        whiteSpace: 'nowrap',
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
    // ── Summary card ─────────────────────────────────────────
    summaryCard: {
        display: 'grid',
        gridTemplateColumns: 'repeat(4, 1fr)',
        gap: '1px',
        background: 'rgba(255,255,255,0.06)',
        border: '1px solid rgba(255,255,255,0.08)',
        borderRadius: '12px',
        overflow: 'hidden',
        marginBottom: '24px',
    },
    summaryCell: {
        padding: '18px 20px',
        background: 'rgba(255,255,255,0.02)',
    },
    summaryLabel: {
        fontSize: '11px',
        fontWeight: '600',
        letterSpacing: '0.6px',
        textTransform: 'uppercase',
        color: 'rgba(255,255,255,0.4)',
        marginBottom: '6px',
    },
    summaryValue: {
        fontSize: '20px',
        fontWeight: '700',
        letterSpacing: '-0.3px',
        color: '#ffffff',
    },
    // ── Tab summary strip ────────────────────────────────────
    tabSummary: {
        display: 'flex',
        gap: '24px',
        padding: '10px 18px',
        background: 'rgba(255,255,255,0.02)',
        borderBottom: '1px solid rgba(255,255,255,0.05)',
        fontSize: '12px',
        color: 'rgba(255,255,255,0.45)',
    },
    tabSummaryItem: {
        display: 'flex',
        alignItems: 'center',
        gap: '6px',
    },
    tabSummaryVal: {
        fontWeight: '600',
        color: 'rgba(255,255,255,0.75)',
    },
    // ── Tabs ────────────────────────────────────────────────
    tabBar: {
        display: 'flex',
        gap: '4px',
        marginBottom: '0',
        borderBottom: '1px solid rgba(255,255,255,0.08)',
        paddingBottom: '0',
    },
    tab: {
        display: 'flex',
        alignItems: 'center',
        gap: '7px',
        padding: '10px 16px',
        fontSize: '13px',
        fontWeight: '500',
        cursor: 'pointer',
        border: 'none',
        background: 'transparent',
        color: 'rgba(255,255,255,0.4)',
        borderBottom: '2px solid transparent',
        marginBottom: '-1px',
        transition: 'color 0.15s, border-color 0.15s',
        letterSpacing: '0.2px',
    },
    tabActive: {
        color: '#ffffff',
        borderBottom: '2px solid #4a9eff',
    },
    tabBadge: {
        display: 'inline-flex',
        alignItems: 'center',
        justifyContent: 'center',
        minWidth: '18px',
        height: '18px',
        padding: '0 5px',
        background: 'rgba(255,255,255,0.1)',
        borderRadius: '20px',
        fontSize: '11px',
        fontWeight: '600',
        color: 'rgba(255,255,255,0.5)',
    },
    tabBadgeActive: {
        background: 'rgba(74,158,255,0.2)',
        color: '#63b3ed',
    },
    // ── Table ───────────────────────────────────────────────
    tableWrapper: {
        background: 'rgba(255,255,255,0.03)',
        border: '1px solid rgba(255,255,255,0.08)',
        borderRadius: '0 0 12px 12px',
        overflow: 'hidden',
    },
    table: {
        width: '100%',
        borderCollapse: 'collapse',
    },
    th: {
        textAlign: 'left',
        padding: '13px 18px',
        fontSize: '12px',
        fontWeight: '600',
        color: 'rgba(255,255,255,0.4)',
        letterSpacing: '0.6px',
        textTransform: 'uppercase',
        borderBottom: '1px solid rgba(255,255,255,0.07)',
        background: 'rgba(255,255,255,0.02)',
    },
    thRight: {
        textAlign: 'right',
        padding: '13px 18px',
        fontSize: '12px',
        fontWeight: '600',
        color: 'rgba(255,255,255,0.4)',
        letterSpacing: '0.6px',
        textTransform: 'uppercase',
        borderBottom: '1px solid rgba(255,255,255,0.07)',
        background: 'rgba(255,255,255,0.02)',
    },
    td: {
        padding: '14px 18px',
        fontSize: '14px',
        color: 'rgba(255,255,255,0.85)',
        borderBottom: '1px solid rgba(255,255,255,0.05)',
    },
    tdRight: {
        padding: '14px 18px',
        fontSize: '14px',
        color: 'rgba(255,255,255,0.85)',
        borderBottom: '1px solid rgba(255,255,255,0.05)',
        textAlign: 'right',
    },
    symbol: {
        fontWeight: '600',
        color: '#ffffff',
        fontSize: '14px',
    },
    typeBadge: {
        display: 'inline-block',
        padding: '3px 10px',
        borderRadius: '20px',
        fontSize: '11px',
        fontWeight: '600',
        letterSpacing: '0.4px',
        textTransform: 'uppercase',
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
    },
    emptyRow: {
        textAlign: 'center',
        padding: '56px 18px',
        color: 'rgba(255,255,255,0.25)',
        fontSize: '14px',
    },
    loadingText: {
        color: 'rgba(255,255,255,0.35)',
        fontSize: '14px',
        textAlign: 'center',
        padding: '48px 0',
    },
    // ── Modal ───────────────────────────────────────────────
    overlay: {
        position: 'fixed',
        inset: 0,
        background: 'rgba(0,0,0,0.65)',
        backdropFilter: 'blur(4px)',
        zIndex: 1000,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        padding: '24px',
    },
    modal: {
        background: '#12122a',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '16px',
        width: '100%',
        maxWidth: '700px',
        padding: '28px',
        position: 'relative',
        boxShadow: '0 24px 80px rgba(0,0,0,0.7)',
    },
    modalHeader: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '24px',
    },
    modalTitle: {
        fontSize: '18px',
        fontWeight: '700',
        letterSpacing: '-0.3px',
    },
    modalSubtitle: {
        fontSize: '12px',
        color: 'rgba(255,255,255,0.35)',
        fontWeight: '400',
        marginTop: '2px',
    },
    closeBtn: {
        background: 'rgba(255,255,255,0.06)',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '8px',
        color: 'rgba(255,255,255,0.65)',
        width: '32px',
        height: '32px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        cursor: 'pointer',
        fontSize: '18px',
        lineHeight: 1,
        flexShrink: 0,
        transition: 'background 0.2s, color 0.2s',
    },
    modalCenter: {
        minHeight: '220px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    },
    modalEmpty: {
        color: 'rgba(255,255,255,0.3)',
        fontSize: '14px',
    },
    rowClickable: {
        cursor: 'pointer',
    },
};

const TYPE_COLORS = {
    STOCK:  { background: 'rgba(74,158,255,0.15)',  color: '#63b3ed' },
    CRYPTO: { background: 'rgba(251,191,36,0.15)',  color: '#fbbf24' },
    BOND:   { background: 'rgba(52,211,153,0.15)',  color: '#34d399' },
};

function typeBadgeStyle(type) {
    const colors = TYPE_COLORS[type?.toUpperCase()] ?? { background: 'rgba(255,255,255,0.08)', color: 'rgba(255,255,255,0.5)' };
    return { ...styles.typeBadge, ...colors };
}

const TABS = [
    { key: 'All',    label: 'All',     filter: null     },
    { key: 'Stock',  label: 'Stocks',  filter: 'STOCK'  },
    { key: 'Bond',   label: 'Bonds',   filter: 'BOND'   },
    { key: 'Crypto', label: 'Crypto',  filter: 'CRYPTO' },
];

// ── Helpers ────────────────────────────────────────────────────
function calcSummary(assets) {
    const priced = assets.filter((a) => a.currentPrice != null);
    const totalInvested = priced.reduce((s, a) => s + a.buyPrice * a.quantity, 0);
    const currentValue  = priced.reduce((s, a) => s + a.currentPrice * a.quantity, 0);
    const pnl           = currentValue - totalInvested;
    const pnlPercentage = totalInvested !== 0 ? (pnl / totalInvested) * 100 : 0;
    return { totalInvested, currentValue, pnl, pnlPercentage };
}

function fmt$(n) { return `$${Number(n).toFixed(2)}`; }
function fmtPct(n) { return `${n >= 0 ? '+' : ''}${Number(n).toFixed(2)}%`; }
function fmtPnl(n) { return `${n >= 0 ? '+' : ''}${fmt$(n)}`; }
function pnlColor(n) { return n >= 0 ? '#4ade80' : '#fc8181'; }

// ── Price history modal ────────────────────────────────────────
function PriceHistoryModal({ symbol, onClose }) {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getPriceHistory(symbol)
            .then((res) => setData(res.data))
            .catch(() => setData([]))
            .finally(() => setLoading(false));
    }, [symbol]);

    const fmtTime = (ts) => {
        const d = new Date(ts);
        return d.toLocaleTimeString('en-GB'); // HH:mm:ss
    };

    const chartData = data.map((p) => ({ time: fmtTime(p.timestamp), price: p.price }));

    const minPrice = chartData.length ? Math.min(...chartData.map((d) => d.price)) : 0;
    const maxPrice = chartData.length ? Math.max(...chartData.map((d) => d.price)) : 0;
    const padding  = (maxPrice - minPrice) * 0.05 || 1;

    return (
        <div style={styles.overlay} onClick={onClose}>
            <div style={styles.modal} onClick={(e) => e.stopPropagation()}>
                <div style={styles.modalHeader}>
                    <div>
                        <div style={styles.modalTitle}>{symbol}</div>
                        <div style={styles.modalSubtitle}>Price history — last hour</div>
                    </div>
                    <button
                        style={styles.closeBtn}
                        onClick={onClose}
                        onMouseEnter={(e) => { e.currentTarget.style.background = 'rgba(255,255,255,0.12)'; e.currentTarget.style.color = '#ffffff'; }}
                        onMouseLeave={(e) => { e.currentTarget.style.background = 'rgba(255,255,255,0.06)'; e.currentTarget.style.color = 'rgba(255,255,255,0.65)'; }}
                    >
                        ×
                    </button>
                </div>

                {loading ? (
                    <div style={styles.modalCenter}>
                        <div style={styles.modalEmpty}>Loading…</div>
                    </div>
                ) : chartData.length === 0 ? (
                    <div style={styles.modalCenter}>
                        <div style={styles.modalEmpty}>No price history available.</div>
                    </div>
                ) : (
                    <ResponsiveContainer width="100%" height={260}>
                        <LineChart data={chartData} margin={{ top: 4, right: 16, left: 0, bottom: 4 }}>
                            <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.06)" />
                            <XAxis
                                dataKey="time"
                                tick={{ fill: 'rgba(255,255,255,0.35)', fontSize: 11 }}
                                tickLine={false}
                                axisLine={{ stroke: 'rgba(255,255,255,0.08)' }}
                                interval="preserveStartEnd"
                            />
                            <YAxis
                                domain={[minPrice - padding, maxPrice + padding]}
                                tick={{ fill: 'rgba(255,255,255,0.35)', fontSize: 11 }}
                                tickLine={false}
                                axisLine={false}
                                tickFormatter={(v) => `$${Number(v).toFixed(2)}`}
                                width={70}
                            />
                            <Tooltip
                                contentStyle={{
                                    background: '#1a1a2e',
                                    border: '1px solid rgba(255,255,255,0.12)',
                                    borderRadius: '8px',
                                    color: '#ffffff',
                                    fontSize: '13px',
                                }}
                                formatter={(v) => [`$${Number(v).toFixed(2)}`, 'Price']}
                                labelStyle={{ color: 'rgba(255,255,255,0.5)', marginBottom: '4px' }}
                            />
                            <Line
                                type="monotone"
                                dataKey="price"
                                stroke="#4a9eff"
                                strokeWidth={2}
                                dot={false}
                                activeDot={{ r: 4, fill: '#4a9eff', stroke: '#12122a', strokeWidth: 2 }}
                            />
                        </LineChart>
                    </ResponsiveContainer>
                )}
            </div>
        </div>
    );
}

// ── Asset table row ────────────────────────────────────────────
function AssetRow({ asset, onDelete, onClick }) {
    const [deleting, setDeleting] = useState(false);
    const [rowHovered, setRowHovered] = useState(false);

    const handleDelete = async (e) => {
        e.stopPropagation();
        setDeleting(true);
        await onDelete(asset.id);
        setDeleting(false);
    };

    const hasPrice = asset.currentPrice != null;

    return (
        <tr
            onMouseEnter={() => setRowHovered(true)}
            onMouseLeave={() => setRowHovered(false)}
            onClick={onClick}
            style={{
                background: rowHovered ? 'rgba(255,255,255,0.03)' : 'transparent',
                transition: 'background 0.15s',
                cursor: 'pointer',
            }}
        >
            <td style={styles.td}><span style={styles.symbol}>{asset.symbol}</span></td>
            <td style={styles.td}><span style={typeBadgeStyle(asset.type)}>{asset.type}</span></td>
            <td style={styles.tdRight}>{asset.quantity}</td>
            <td style={styles.tdRight}>{fmt$(asset.buyPrice)}</td>
            <td style={styles.tdRight}>
                {hasPrice ? fmt$(asset.currentPrice) : 'N/A'}
            </td>
            <td style={{ ...styles.tdRight, color: hasPrice ? pnlColor(asset.pnl) : 'rgba(255,255,255,0.35)' }}>
                {hasPrice ? fmtPnl(asset.pnl) : 'N/A'}
            </td>
            <td style={{ ...styles.tdRight, color: hasPrice ? pnlColor(asset.pnlPercentage) : 'rgba(255,255,255,0.35)' }}>
                {hasPrice ? fmtPct(asset.pnlPercentage) : 'N/A'}
            </td>
            <td style={styles.tdRight}>
                <button
                    style={{ ...styles.deleteBtn, opacity: deleting ? 0.5 : 1, cursor: deleting ? 'not-allowed' : 'pointer' }}
                    onClick={handleDelete}
                    disabled={deleting}
                >
                    {deleting ? '...' : 'Delete'}
                </button>
            </td>
        </tr>
    );
}

// ── Summary card ───────────────────────────────────────────────
function SummaryCard({ assets }) {
    const { totalInvested, currentValue, pnl, pnlPercentage } = calcSummary(assets);
    const color = pnlColor(pnl);

    return (
        <div style={styles.summaryCard}>
            <div style={styles.summaryCell}>
                <div style={styles.summaryLabel}>Invested</div>
                <div style={styles.summaryValue}>{fmt$(totalInvested)}</div>
            </div>
            <div style={styles.summaryCell}>
                <div style={styles.summaryLabel}>Current Value</div>
                <div style={styles.summaryValue}>{fmt$(currentValue)}</div>
            </div>
            <div style={styles.summaryCell}>
                <div style={styles.summaryLabel}>P&amp;L</div>
                <div style={{ ...styles.summaryValue, color }}>{fmtPnl(pnl)}</div>
            </div>
            <div style={styles.summaryCell}>
                <div style={styles.summaryLabel}>Return</div>
                <div style={{ ...styles.summaryValue, color }}>{fmtPct(pnlPercentage)}</div>
            </div>
        </div>
    );
}

// ── Tab P&L strip (shown for Stocks / Bonds / Crypto tabs) ─────
function TabSummaryStrip({ assets, filter }) {
    const filtered = assets.filter((a) => a.type === filter);
    const { totalInvested, currentValue, pnl, pnlPercentage } = calcSummary(filtered);
    const color = pnlColor(pnl);

    return (
        <div style={styles.tabSummary}>
            <div style={styles.tabSummaryItem}>
                Invested: <span style={styles.tabSummaryVal}>{fmt$(totalInvested)}</span>
            </div>
            <div style={styles.tabSummaryItem}>
                Value: <span style={styles.tabSummaryVal}>{fmt$(currentValue)}</span>
            </div>
            <div style={styles.tabSummaryItem}>
                P&amp;L: <span style={{ ...styles.tabSummaryVal, color }}>{fmtPnl(pnl)}</span>
            </div>
            <div style={styles.tabSummaryItem}>
                Return: <span style={{ ...styles.tabSummaryVal, color }}>{fmtPct(pnlPercentage)}</span>
            </div>
        </div>
    );
}

// ── Page ──────────────────────────────────────────────────────
export default function PortfolioDetailPage() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [portfolioName, setPortfolioName] = useState('');
    const [assets, setAssets] = useState([]);
    const [activeTab, setActiveTab] = useState('All');
    const [selectedAsset, setSelectedAsset] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        Promise.all([getPortfolioById(id), getAssetsPnl(id)])
            .then(([portfolioRes, assetsRes]) => {
                setPortfolioName(portfolioRes.data.name);
                setAssets(assetsRes.data);
            })
            .catch(() => setError('Failed to load portfolio.'))
            .finally(() => setLoading(false));
    }, [id]);

    const handlePnlUpdate = useCallback((dto) => {
        setAssets((prev) =>
            prev.map((a) => (a.id === dto.id ? { ...a, ...dto } : a))
        );
    }, []);

    usePnlWebSocket(handlePnlUpdate);

    const handleDelete = async (assetId) => {
        setError('');
        try {
            await deleteAsset(assetId);
            setAssets((prev) => prev.filter((a) => a.id !== assetId));
        } catch {
            setError('Failed to delete asset.');
        }
    };

    const counts = {
        All:    assets.length,
        Stock:  assets.filter((a) => a.type === 'STOCK').length,
        Bond:   assets.filter((a) => a.type === 'BOND').length,
        Crypto: assets.filter((a) => a.type === 'CRYPTO').length,
    };

    const activeFilter = TABS.find((t) => t.key === activeTab)?.filter;
    const visibleAssets = activeFilter ? assets.filter((a) => a.type === activeFilter) : assets;

    return (
        <div style={styles.page}>
            {selectedAsset && (
                <PriceHistoryModal
                    symbol={selectedAsset.symbol}
                    onClose={() => setSelectedAsset(null)}
                />
            )}
            <nav style={styles.navbar}>
                <button
                    style={styles.backBtn}
                    onClick={() => navigate('/dashboard')}
                    onMouseEnter={(e) => { e.currentTarget.style.background = 'rgba(255,255,255,0.1)'; e.currentTarget.style.color = '#ffffff'; }}
                    onMouseLeave={(e) => { e.currentTarget.style.background = 'rgba(255,255,255,0.06)'; e.currentTarget.style.color = 'rgba(255,255,255,0.65)'; }}
                >
                    ← Back
                </button>
                <span style={styles.brand}>Portfolio Tracker</span>
            </nav>

            <main style={styles.main}>
                <div style={styles.pageHeader}>
                    <h2 style={styles.heading}>{portfolioName}</h2>
                    <button
                        style={styles.addBtn}
                        onClick={() => navigate(`/portfolio/${id}/add-asset`)}
                        onMouseEnter={(e) => { e.currentTarget.style.opacity = '0.85'; }}
                        onMouseLeave={(e) => { e.currentTarget.style.opacity = '1'; }}
                    >
                        + Add Asset
                    </button>
                </div>

                {error && <div style={styles.error}>{error}</div>}

                {loading ? (
                    <p style={styles.loadingText}>Loading assets…</p>
                ) : (
                    <>
                        <SummaryCard assets={assets} />

                        <div style={styles.tabBar}>
                            {TABS.map((tab) => {
                                const isActive = activeTab === tab.key;
                                return (
                                    <button
                                        key={tab.key}
                                        style={{ ...styles.tab, ...(isActive ? styles.tabActive : {}) }}
                                        onClick={() => setActiveTab(tab.key)}
                                        onMouseEnter={(e) => { if (!isActive) e.currentTarget.style.color = 'rgba(255,255,255,0.65)'; }}
                                        onMouseLeave={(e) => { if (!isActive) e.currentTarget.style.color = 'rgba(255,255,255,0.4)'; }}
                                    >
                                        {tab.label}
                                        <span style={{ ...styles.tabBadge, ...(isActive ? styles.tabBadgeActive : {}) }}>
                                            {counts[tab.key]}
                                        </span>
                                    </button>
                                );
                            })}
                        </div>

                        <div style={styles.tableWrapper}>
                            {activeFilter && (
                                <TabSummaryStrip assets={assets} filter={activeFilter} />
                            )}
                            <table style={styles.table}>
                                <thead>
                                    <tr>
                                        <th style={styles.th}>Symbol</th>
                                        <th style={styles.th}>Type</th>
                                        <th style={styles.thRight}>Quantity</th>
                                        <th style={styles.thRight}>Buy Price</th>
                                        <th style={styles.thRight}>Current Price</th>
                                        <th style={styles.thRight}>P&amp;L</th>
                                        <th style={styles.thRight}>Return</th>
                                        <th style={styles.thRight}>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {visibleAssets.length === 0 ? (
                                        <tr>
                                            <td colSpan={8} style={styles.emptyRow}>
                                                {assets.length === 0
                                                    ? 'No assets yet. Add one with the button above.'
                                                    : `No ${activeTab.toLowerCase()} assets in this portfolio.`}
                                            </td>
                                        </tr>
                                    ) : (
                                        visibleAssets.map((asset) => (
                                            <AssetRow key={asset.id} asset={asset} onDelete={handleDelete} onClick={() => setSelectedAsset(asset)} />
                                        ))
                                    )}
                                </tbody>
                            </table>
                        </div>
                    </>
                )}
            </main>
        </div>
    );
}
