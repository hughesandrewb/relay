import { Headers, ContentType } from '$lib/api/constants';
import { authStore } from '$lib/stores/auth.svelte';
import ApiError from '$lib/error/apiError';

interface RequestOptions {
	method?: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	body?: any;
	headers?: Record<string, string>;
}

const BASE_URL: string = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export async function request(endpoint: string, options: RequestOptions = {}) {
	const url = `${BASE_URL}${endpoint}`;
	const token: string | null = authStore.getToken();

	const { method = 'GET', body, headers: customHeaders = {} } = options;

	if (!token) {
		throw new ApiError(403, 'Unauthenticated');
	}

	const config: RequestInit = {
		method,
		headers: {
			[Headers.CONTENT_TYPE]: ContentType.JSON,
			[Headers.AUTHORIZATION]: `Bearer ${token}`,
			[Headers.ACCEPT]: ContentType.JSON,
			...customHeaders
		}
	};

	if (body && ['POST', 'PUT', 'PATCH'].includes(method)) {
		config.body = JSON.stringify(body);
	}

	const response = await fetch(url, config);

	if (!response.ok) {
		const err = await response.json().catch(() => ({}));
		throw new ApiError(response.status, err.message);
	}

	if (response.status === 204) {
		return null;
	}

	return response.json();
}
