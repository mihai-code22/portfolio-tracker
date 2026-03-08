import { useEffect, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const WS_URL = 'http://localhost:8080/ws';
const RECONNECT_DELAY_MS = 5000;

export function usePnlWebSocket(onPnlUpdate) {
    // Keep callback ref up-to-date on every render so the subscription
    // closure never goes stale, without triggering a reconnect.
    const callbackRef = useRef(onPnlUpdate);
    useEffect(() => {
        callbackRef.current = onPnlUpdate;
    });

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) return;

        const client = new Client({
            webSocketFactory: () => new SockJS(WS_URL),
            connectHeaders: {
                Authorization: 'Bearer ' + token,
            },
            reconnectDelay: RECONNECT_DELAY_MS,
            onConnect: () => {
                client.subscribe('/user/queue/pnl', (message) => {
                    try {
                        const dto = JSON.parse(message.body);
                        callbackRef.current(dto);
                    } catch (e) {
                        console.error('Failed to parse P&L update:', e);
                    }
                });
            },
            onStompError: (frame) => {
                console.error('STOMP error:', frame.headers?.message, frame.body);
            },
        });

        client.activate();

        return () => {
            client.deactivate();
        };
    }, []); // connect once; reconnect is handled internally by the client
}
