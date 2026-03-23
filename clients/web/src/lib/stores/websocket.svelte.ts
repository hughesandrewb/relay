import { browser } from '$app/environment';
import { realtimeApi } from '$lib/api/resources/realtime';
import type { RealtimeTicket } from '$lib/models/realtime';

const MAX_RECONNECT_ATTEMPTS = 5;
const RECONNECT_DELAY = 3000;

export type GatewayEvent = 'MESSAGAE_CREATE';

const GatewayOpcode = {
	Dispatch: 0,
	Heartbeat: 1,
	Identify: 2,
	PresenceUpdate: 3,
	Resume: 6,
	Reconnect: 7,
	InvalidSession: 9,
	Hello: 10,
	HeartbeatAck: 11
} as const;

type GatewayOpcode = typeof GatewayOpcode[keyof typeof GatewayOpcode];

type ConnectionStatus = 'disconnected' | 'connecting' | 'connected' | 'error';

interface RealtimeMessage<T = unknown> {
	op: GatewayOpcode;
	d: T;
	s: number;
	t: GatewayEvent;
}

type MessageHandler<T = unknown> = (data: T) => void;
type UnsubscribeFn = () => void;

class WebSocketStore {
	status: ConnectionStatus = $state('disconnected');
	socket: WebSocket | null = $state(null);
	reconnectionAttempts: number = $state(0);

	private reconnectTimeout: ReturnType<typeof setTimeout> | null = null;
	private handlers = new Map<GatewayEvent, Set<MessageHandler>>();
	private wildcardHandlers = new Set<MessageHandler>();

	on<T>(t: GatewayEvent, handler: MessageHandler<T>): UnsubscribeFn {
		let opHandlers = this.handlers.get(t);
		if (!opHandlers) {
			// eslint-disable-next-line svelte/prefer-svelte-reactivity
			opHandlers = new Set<MessageHandler>();
			this.handlers.set(t, opHandlers);
		}

		opHandlers.add(handler as MessageHandler);
		return () => opHandlers.delete(handler as MessageHandler);
	}

	onAny(handler: MessageHandler<RealtimeMessage>): UnsubscribeFn {
		this.wildcardHandlers.add(handler as MessageHandler);
		return () => this.wildcardHandlers.delete(handler as MessageHandler);
	}

	clearHandlers(t?: GatewayEvent) {
		if (t) {
			this.handlers.delete(t);
			return;
		}
		this.handlers.clear();
		this.wildcardHandlers.clear();
	}

	private dispatch(message: RealtimeMessage) {
		this.wildcardHandlers.forEach((handler) => handler(message));

		const opHandlers: Set<MessageHandler> | undefined = this.handlers.get(message.t);

		if (!opHandlers || opHandlers.size === 0) {
			console.warn(`No handlers registered for event: ${message.t}`);
			return;
		}

		opHandlers.forEach((handler) => handler(message.d));
	}

	private handleMessage = (event: MessageEvent) => {
		try {
			const message: RealtimeMessage = JSON.parse(event.data);
			this.dispatch(message);
		} catch (e) {
			console.log('Failed to parse message:', e);
		}
	};

	async connect(url: string) {
		if (!browser) return;

		if (this.socket?.readyState === WebSocket.OPEN) {
			console.log('Already connected');
			return;
		}

		this.status = 'connecting';

		const ticket: RealtimeTicket = await realtimeApi.getRealtimeTicket();
		const ticketedUrl = `${url}?ticket=${ticket.code}`;

		this.socket = new WebSocket(ticketedUrl);

		this.socket.onmessage = this.handleMessage;

		this.socket.onopen = () => {
			console.log('WebSocket connected');
			this.status = 'connected';
			this.reconnectionAttempts = 0;
		};

		this.socket.onclose = () => {
			console.log('WebSocket disconnected');

			this.status = 'disconnected';
			this.reconnectionAttempts += 1;

			if (this.reconnectionAttempts < MAX_RECONNECT_ATTEMPTS) {
				this.reconnectTimeout = setTimeout(() => {
					console.log(`Reconnecting... (attempt ${this.reconnectionAttempts})`);
					this.connect(url);
				}, RECONNECT_DELAY);
			}
		};

		this.socket.onerror = (error) => {
			console.log('WebSocket error: ', error);

			this.status = 'error';
		};
	}

	disconnect() {
		this.socket?.close();

		if (this.reconnectTimeout) {
			clearTimeout(this.reconnectTimeout);
		}

		this.status = 'disconnected';
	}

	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	send(data: any) {
		if (this.socket?.readyState === WebSocket.OPEN) {
			this.socket.send(JSON.stringify(data));
		} else {
			console.warn('WebSocket not connected');
		}
	}
}

export const wsStore = new WebSocketStore();
