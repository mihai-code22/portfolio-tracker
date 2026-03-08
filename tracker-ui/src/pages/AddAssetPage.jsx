import { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { createAsset } from '../services/api';

const SECTORS = [
    'HEALTHCARE', 'FINANCE', 'ENERGY', 'CONSUMER_GOODS',
    'INDUSTRIALS', 'UTILITIES', 'REAL_ESTATE', 'MATERIALS',
    'COMMUNICATION_SERVICES', 'INFORMATION_TECHNOLOGY',
];

const TYPE_OPTIONS = [
    { value: 'STOCK',  label: 'Stock'  },
    { value: 'BOND',   label: 'Bond'   },
    { value: 'CRYPTO', label: 'Crypto' },
];

const SECTOR_OPTIONS = SECTORS.map((s) => ({ value: s, label: s.replace(/_/g, ' ') }));

const EMPTY_FORM = {
    symbol: '', quantity: '', buyPrice: '', type: 'STOCK',
    exchange: '', sector: '', couponRate: '', maturityDate: '', blockchain: '',
};

const TYPE_SPECIFIC_FIELDS = ['exchange', 'sector', 'couponRate', 'maturityDate', 'blockchain'];

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
        maxWidth: '560px',
        margin: '0 auto',
        padding: '40px 24px',
    },
    heading: {
        fontSize: '22px',
        fontWeight: '700',
        margin: '0 0 28px 0',
        letterSpacing: '-0.4px',
    },
    error: {
        background: 'rgba(239,68,68,0.1)',
        border: '1px solid rgba(239,68,68,0.25)',
        borderRadius: '8px',
        color: '#fc8181',
        fontSize: '13px',
        padding: '10px 14px',
        marginBottom: '16px',
        lineHeight: '1.5',
    },
    formCard: {
        background: 'rgba(255,255,255,0.03)',
        border: '1px solid rgba(255,255,255,0.08)',
        borderRadius: '12px',
        padding: '24px',
    },
    formGrid: {
        display: 'grid',
        gridTemplateColumns: '1fr 1fr',
        gap: '12px',
    },
    formGroup: {
        display: 'flex',
        flexDirection: 'column',
        gap: '6px',
    },
    formLabel: {
        fontSize: '12px',
        fontWeight: '500',
        color: 'rgba(255,255,255,0.4)',
        letterSpacing: '0.3px',
    },
    formInput: {
        padding: '10px 12px',
        background: 'rgba(255,255,255,0.06)',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '8px',
        color: '#ffffff',
        fontSize: '14px',
        outline: 'none',
        boxSizing: 'border-box',
        width: '100%',
        transition: 'border-color 0.2s, background 0.2s',
    },
    formInputFocus: {
        borderColor: 'rgba(99,179,237,0.55)',
        background: 'rgba(255,255,255,0.09)',
    },
    formDivider: {
        height: '1px',
        background: 'rgba(255,255,255,0.06)',
        margin: '16px 0',
    },
    formActions: {
        marginTop: '20px',
        display: 'flex',
        justifyContent: 'flex-end',
        gap: '10px',
    },
    cancelBtn: {
        padding: '10px 20px',
        background: 'rgba(255,255,255,0.06)',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '8px',
        color: 'rgba(255,255,255,0.65)',
        fontSize: '14px',
        fontWeight: '500',
        cursor: 'pointer',
        transition: 'background 0.2s, color 0.2s',
    },
    submitBtn: {
        padding: '10px 22px',
        background: 'linear-gradient(135deg, #4a9eff 0%, #7b5ea7 100%)',
        border: 'none',
        borderRadius: '8px',
        color: '#ffffff',
        fontSize: '14px',
        fontWeight: '600',
        cursor: 'pointer',
        transition: 'opacity 0.2s',
        letterSpacing: '0.2px',
    },
    // ── Custom Dropdown ──────────────────────────────────────
    ddWrapper: { position: 'relative' },
    ddTrigger: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        padding: '10px 12px',
        background: 'rgba(255,255,255,0.06)',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '8px',
        color: '#ffffff',
        fontSize: '14px',
        cursor: 'pointer',
        userSelect: 'none',
        transition: 'border-color 0.2s, background 0.2s',
        boxSizing: 'border-box',
    },
    ddTriggerOpen: {
        borderColor: 'rgba(99,179,237,0.55)',
        background: 'rgba(255,255,255,0.09)',
    },
    ddChevron: {
        fontSize: '9px',
        color: 'rgba(255,255,255,0.35)',
        marginLeft: '8px',
        flexShrink: 0,
        transition: 'transform 0.2s',
    },
    ddMenu: {
        position: 'absolute',
        top: 'calc(100% + 4px)',
        left: 0,
        right: 0,
        background: '#12122a',
        border: '1px solid rgba(255,255,255,0.12)',
        borderRadius: '8px',
        zIndex: 200,
        maxHeight: '220px',
        overflowY: 'auto',
        boxShadow: '0 12px 40px rgba(0,0,0,0.6)',
    },
    ddOption: {
        padding: '9px 12px',
        fontSize: '14px',
        color: 'rgba(255,255,255,0.7)',
        cursor: 'pointer',
    },
    ddOptionActive: {
        background: 'rgba(74,158,255,0.15)',
        color: '#63b3ed',
        fontWeight: '500',
    },
};

function Dropdown({ value, onChange, options, placeholder = 'Select…' }) {
    const [open, setOpen] = useState(false);
    const ref = useRef(null);

    useEffect(() => {
        if (!open) return;
        const handler = (e) => {
            if (ref.current && !ref.current.contains(e.target)) setOpen(false);
        };
        document.addEventListener('mousedown', handler);
        return () => document.removeEventListener('mousedown', handler);
    }, [open]);

    const selected = options.find((o) => o.value === value);

    return (
        <div ref={ref} style={styles.ddWrapper}>
            <div
                style={{ ...styles.ddTrigger, ...(open ? styles.ddTriggerOpen : {}) }}
                onClick={() => setOpen((p) => !p)}
            >
                <span style={{ color: selected ? '#ffffff' : 'rgba(255,255,255,0.3)' }}>
                    {selected ? selected.label : placeholder}
                </span>
                <span style={{ ...styles.ddChevron, transform: open ? 'rotate(180deg)' : 'rotate(0deg)' }}>
                    ▼
                </span>
            </div>

            {open && (
                <div style={styles.ddMenu}>
                    {options.map((o) => {
                        const active = o.value === value;
                        return (
                            <div
                                key={o.value}
                                style={{ ...styles.ddOption, ...(active ? styles.ddOptionActive : {}) }}
                                onMouseEnter={(e) => { if (!active) e.currentTarget.style.background = 'rgba(255,255,255,0.05)'; }}
                                onMouseLeave={(e) => { if (!active) e.currentTarget.style.background = 'transparent'; }}
                                onClick={() => { onChange(o.value); setOpen(false); }}
                            >
                                {o.label}
                            </div>
                        );
                    })}
                </div>
            )}
        </div>
    );
}

function FormField({ label, children }) {
    return (
        <div style={styles.formGroup}>
            <label style={styles.formLabel}>{label}</label>
            {children}
        </div>
    );
}

function FormInput({ value, onChange, type = 'text', placeholder, min }) {
    const [focused, setFocused] = useState(false);
    return (
        <input
            type={type}
            value={value}
            onChange={onChange}
            placeholder={placeholder}
            min={min}
            step={type === 'number' ? 'any' : undefined}
            onFocus={() => setFocused(true)}
            onBlur={() => setFocused(false)}
            style={{ ...styles.formInput, ...(focused ? styles.formInputFocus : {}) }}
        />
    );
}

export default function AddAssetPage() {
    const { id: portfolioId } = useParams();
    const navigate = useNavigate();

    const [form, setForm] = useState(EMPTY_FORM);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState('');

    const setInput = (field) => (e) => setForm((prev) => ({ ...prev, [field]: e.target.value }));
    const setField = (field) => (value) => setForm((prev) => ({ ...prev, [field]: value }));
    const setType  = (value) => setForm((prev) => ({
        ...prev,
        type: value,
        ...Object.fromEntries(TYPE_SPECIFIC_FIELDS.map((f) => [f, ''])),
    }));

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSubmitting(true);

        const payload = {
            symbol:   form.symbol.trim().toUpperCase(),
            quantity: Number(form.quantity),
            buyPrice: Number(form.buyPrice),
            type:     form.type,
        };
        if (form.type === 'STOCK')  { payload.exchange = form.exchange.trim(); payload.sector = form.sector; }
        if (form.type === 'BOND')   { payload.couponRate = Number(form.couponRate); payload.maturityDate = form.maturityDate; }
        if (form.type === 'CRYPTO') { payload.blockchain = form.blockchain.trim(); }

        try {
            await createAsset(portfolioId, payload);
            navigate(-1);
        } catch (err) {
            const msg = err?.response?.data?.message || err?.response?.data || err?.message;
            setError(typeof msg === 'string' ? msg : 'Failed to create asset.');
        } finally {
            setSubmitting(false);
        }
    };

    const canSubmit = form.symbol.trim() && form.quantity && form.buyPrice
        && (form.type !== 'STOCK'  || (form.exchange.trim() && form.sector))
        && (form.type !== 'BOND'   || (form.couponRate && form.maturityDate))
        && (form.type !== 'CRYPTO' || form.blockchain.trim());

    return (
        <div style={styles.page}>
            <nav style={styles.navbar}>
                <button
                    style={styles.backBtn}
                    onClick={() => navigate(-1)}
                    onMouseEnter={(e) => { e.currentTarget.style.background = 'rgba(255,255,255,0.1)'; e.currentTarget.style.color = '#ffffff'; }}
                    onMouseLeave={(e) => { e.currentTarget.style.background = 'rgba(255,255,255,0.06)'; e.currentTarget.style.color = 'rgba(255,255,255,0.65)'; }}
                >
                    ← Back
                </button>
                <span style={styles.brand}>Portfolio Tracker</span>
            </nav>

            <main style={styles.main}>
                <h2 style={styles.heading}>Add Asset</h2>

                <div style={styles.formCard}>
                    {error && <div style={styles.error}>{error}</div>}

                    <form onSubmit={handleSubmit} noValidate>
                        {/* Base fields */}
                        <div style={styles.formGrid}>
                            <FormField label="Symbol">
                                <FormInput value={form.symbol} onChange={setInput('symbol')} placeholder="e.g. AAPL" />
                            </FormField>
                            <FormField label="Type">
                                <Dropdown value={form.type} onChange={setType} options={TYPE_OPTIONS} />
                            </FormField>
                            <FormField label="Quantity">
                                <FormInput type="number" value={form.quantity} onChange={setInput('quantity')} placeholder="0" min="0" />
                            </FormField>
                            <FormField label="Buy Price ($)">
                                <FormInput type="number" value={form.buyPrice} onChange={setInput('buyPrice')} placeholder="0.00" min="0" />
                            </FormField>
                        </div>

                        {/* STOCK extras */}
                        {form.type === 'STOCK' && (
                            <>
                                <div style={styles.formDivider} />
                                <div style={styles.formGrid}>
                                    <FormField label="Exchange">
                                        <FormInput value={form.exchange} onChange={setInput('exchange')} placeholder="e.g. NASDAQ" />
                                    </FormField>
                                    <FormField label="Sector">
                                        <Dropdown
                                            value={form.sector}
                                            onChange={setField('sector')}
                                            options={SECTOR_OPTIONS}
                                            placeholder="Select sector…"
                                        />
                                    </FormField>
                                </div>
                            </>
                        )}

                        {/* BOND extras */}
                        {form.type === 'BOND' && (
                            <>
                                <div style={styles.formDivider} />
                                <div style={styles.formGrid}>
                                    <FormField label="Coupon Rate (%)">
                                        <FormInput type="number" value={form.couponRate} onChange={setInput('couponRate')} placeholder="0.00" min="0" />
                                    </FormField>
                                    <FormField label="Maturity Date">
                                        <FormInput type="date" value={form.maturityDate} onChange={setInput('maturityDate')} />
                                    </FormField>
                                </div>
                            </>
                        )}

                        {/* CRYPTO extras */}
                        {form.type === 'CRYPTO' && (
                            <>
                                <div style={styles.formDivider} />
                                <div style={{ ...styles.formGrid, gridTemplateColumns: '1fr' }}>
                                    <FormField label="Blockchain">
                                        <FormInput value={form.blockchain} onChange={setInput('blockchain')} placeholder="e.g. Ethereum" />
                                    </FormField>
                                </div>
                            </>
                        )}

                        <div style={styles.formActions}>
                            <button
                                type="button"
                                style={styles.cancelBtn}
                                onClick={() => navigate(-1)}
                                onMouseEnter={(e) => { e.currentTarget.style.background = 'rgba(255,255,255,0.1)'; e.currentTarget.style.color = '#ffffff'; }}
                                onMouseLeave={(e) => { e.currentTarget.style.background = 'rgba(255,255,255,0.06)'; e.currentTarget.style.color = 'rgba(255,255,255,0.65)'; }}
                            >
                                Cancel
                            </button>
                            <button
                                type="submit"
                                disabled={submitting || !canSubmit}
                                style={{
                                    ...styles.submitBtn,
                                    opacity: submitting || !canSubmit ? 0.5 : 1,
                                    cursor:  submitting || !canSubmit ? 'not-allowed' : 'pointer',
                                }}
                            >
                                {submitting ? 'Adding…' : '+ Add Asset'}
                            </button>
                        </div>
                    </form>
                </div>
            </main>
        </div>
    );
}
