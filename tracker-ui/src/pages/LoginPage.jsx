import { useState } from 'react';
import { login, register } from '../services/api';

const styles = {
    page: {
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        background: 'linear-gradient(135deg, #0f0f1a 0%, #1a1a2e 50%, #16213e 100%)',
        fontFamily: "'Segoe UI', system-ui, -apple-system, sans-serif",
    },
    card: {
        background: 'rgba(255, 255, 255, 0.04)',
        border: '1px solid rgba(255, 255, 255, 0.08)',
        borderRadius: '16px',
        padding: '40px',
        width: '100%',
        maxWidth: '400px',
        backdropFilter: 'blur(20px)',
        boxShadow: '0 24px 64px rgba(0, 0, 0, 0.5)',
    },
    title: {
        color: '#ffffff',
        fontSize: '26px',
        fontWeight: '700',
        margin: '0 0 6px 0',
        letterSpacing: '-0.5px',
    },
    subtitle: {
        color: 'rgba(255,255,255,0.4)',
        fontSize: '14px',
        margin: '0 0 32px 0',
    },
    group: {
        marginBottom: '18px',
    },
    label: {
        display: 'block',
        color: 'rgba(255,255,255,0.6)',
        fontSize: '13px',
        fontWeight: '500',
        marginBottom: '8px',
        letterSpacing: '0.3px',
    },
    input: {
        width: '100%',
        padding: '12px 14px',
        background: 'rgba(255,255,255,0.06)',
        border: '1px solid rgba(255,255,255,0.1)',
        borderRadius: '8px',
        color: '#ffffff',
        fontSize: '15px',
        outline: 'none',
        boxSizing: 'border-box',
        transition: 'border-color 0.2s, background 0.2s',
    },
    inputFocus: {
        borderColor: 'rgba(99, 179, 237, 0.6)',
        background: 'rgba(255,255,255,0.09)',
    },
    button: {
        width: '100%',
        padding: '13px',
        background: 'linear-gradient(135deg, #4a9eff 0%, #7b5ea7 100%)',
        border: 'none',
        borderRadius: '8px',
        color: '#ffffff',
        fontSize: '15px',
        fontWeight: '600',
        cursor: 'pointer',
        marginTop: '8px',
        transition: 'opacity 0.2s, transform 0.1s',
        letterSpacing: '0.3px',
    },
    buttonDisabled: {
        opacity: 0.6,
        cursor: 'not-allowed',
    },
    errorBox: {
        background: 'rgba(239, 68, 68, 0.12)',
        border: '1px solid rgba(239, 68, 68, 0.3)',
        borderRadius: '8px',
        color: '#fc8181',
        fontSize: '13px',
        padding: '10px 14px',
        marginBottom: '18px',
        lineHeight: '1.6',
    },
    toggle: {
        marginTop: '24px',
        textAlign: 'center',
        color: 'rgba(255,255,255,0.4)',
        fontSize: '14px',
    },
    toggleLink: {
        color: '#63b3ed',
        background: 'none',
        border: 'none',
        cursor: 'pointer',
        fontSize: '14px',
        fontWeight: '600',
        padding: '0',
        marginLeft: '4px',
        textDecoration: 'underline',
        textUnderlineOffset: '2px',
    },
    divider: {
        height: '1px',
        background: 'rgba(255,255,255,0.07)',
        margin: '28px 0',
    },
    requirements: {
        marginTop: '8px',
        padding: '10px 12px',
        background: 'rgba(255,255,255,0.04)',
        border: '1px solid rgba(255,255,255,0.08)',
        borderRadius: '8px',
    },
    requirementItem: {
        fontSize: '12px',
        padding: '2px 0',
        display: 'flex',
        alignItems: 'center',
        gap: '6px',
    },
};

const PASSWORD_RULES = [
    { label: 'At least 8 characters',                    test: (p) => p.length >= 8 },
    { label: 'At least one uppercase letter',             test: (p) => /[A-Z]/.test(p) },
    { label: 'At least one lowercase letter',             test: (p) => /[a-z]/.test(p) },
    { label: 'At least one digit',                        test: (p) => /\d/.test(p) },
    { label: 'At least one special character (@$!%*?&)',  test: (p) => /[@$!%*?&]/.test(p) },
];

function InputField({ label, id, type = 'text', value, onChange, placeholder, autoComplete }) {
    const [focused, setFocused] = useState(false);
    return (
        <div style={styles.group}>
            <label htmlFor={id} style={styles.label}>{label}</label>
            <input
                id={id}
                type={type}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                autoComplete={autoComplete}
                onFocus={() => setFocused(true)}
                onBlur={() => setFocused(false)}
                style={{
                    ...styles.input,
                    ...(focused ? styles.inputFocus : {}),
                }}
            />
        </div>
    );
}

function PasswordField({ value, onChange }) {
    const [focused, setFocused] = useState(false);
    return (
        <div style={styles.group}>
            <label htmlFor="password" style={styles.label}>Password</label>
            <input
                id="password"
                type="password"
                value={value}
                onChange={onChange}
                placeholder="Enter your password"
                autoComplete="new-password"
                onFocus={() => setFocused(true)}
                onBlur={() => setFocused(false)}
                style={{
                    ...styles.input,
                    ...(focused ? styles.inputFocus : {}),
                }}
            />
            {focused && (
                <div style={styles.requirements}>
                    {PASSWORD_RULES.map((rule, i) => {
                        const passed = rule.test(value);
                        return (
                            <div key={i} style={{ ...styles.requirementItem, color: passed ? '#68d391' : 'rgba(255,255,255,0.45)' }}>
                                <span>{passed ? '✓' : '○'}</span>
                                <span>{rule.label}</span>
                            </div>
                        );
                    })}
                </div>
            )}
        </div>
    );
}

export default function LoginPage() {
    const [isRegister, setIsRegister] = useState(false);
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState([]);
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrors([]);
        setLoading(true);

        try {
            if (isRegister) {
                await register(username, email, password);
                const loginRes = await login(username, password);
                localStorage.setItem('token', loginRes.data.token);
            } else {
                const res = await login(username, password);
                localStorage.setItem('token', res.data.token);
            }
            window.location.href = '/dashboard';
        } catch (err) {
            const data = err?.response?.data;
            if (data?.errors && Array.isArray(data.errors)) {
                setErrors(data.errors);
            } else {
                const msg = data?.errorCause || data?.message || err?.message || 'Something went wrong. Please try again.';
                setErrors([typeof msg === 'string' ? msg : 'Something went wrong. Please try again.']);
            }
        } finally {
            setLoading(false);
        }
    };

    const switchMode = () => {
        setIsRegister((prev) => !prev);
        setErrors([]);
    };

    return (
        <div style={styles.page}>
            <div style={styles.card}>
                <h1 style={styles.title}>{isRegister ? 'Create account' : 'Welcome back'}</h1>
                <p style={styles.subtitle}>
                    {isRegister ? 'Start tracking your portfolio today.' : 'Sign in to your portfolio tracker.'}
                </p>

                {errors.length > 0 && (
                    <div style={styles.errorBox}>
                        {errors.map((msg, i) => (
                            <div key={i}>{msg}</div>
                        ))}
                    </div>
                )}

                <form onSubmit={handleSubmit} noValidate>
                    <InputField
                        label="Username"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="Enter your username"
                        autoComplete="username"
                    />

                    {isRegister && (
                        <InputField
                            label="Email"
                            id="email"
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="Enter your email"
                            autoComplete="email"
                        />
                    )}

                    {isRegister ? (
                        <PasswordField value={password} onChange={(e) => setPassword(e.target.value)} />
                    ) : (
                        <InputField
                            label="Password"
                            id="password"
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="Enter your password"
                            autoComplete="current-password"
                        />
                    )}

                    <button
                        type="submit"
                        disabled={loading}
                        style={{
                            ...styles.button,
                            ...(loading ? styles.buttonDisabled : {}),
                        }}
                        onMouseEnter={(e) => { if (!loading) e.currentTarget.style.opacity = '0.88'; }}
                        onMouseLeave={(e) => { e.currentTarget.style.opacity = '1'; }}
                        onMouseDown={(e) => { if (!loading) e.currentTarget.style.transform = 'scale(0.98)'; }}
                        onMouseUp={(e) => { e.currentTarget.style.transform = 'scale(1)'; }}
                    >
                        {loading ? (isRegister ? 'Creating account...' : 'Signing in...') : (isRegister ? 'Create account' : 'Sign in')}
                    </button>
                </form>

                <div style={styles.divider} />

                <div style={styles.toggle}>
                    {isRegister ? 'Already have an account?' : "Don't have an account?"}
                    <button style={styles.toggleLink} onClick={switchMode}>
                        {isRegister ? 'Sign in' : 'Register'}
                    </button>
                </div>
            </div>
        </div>
    );
}
