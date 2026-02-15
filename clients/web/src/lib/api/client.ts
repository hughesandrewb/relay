import createClient from 'openapi-fetch';
import type { paths } from '$lib/api/schema';
import { authStore } from '$lib/stores/auth.svelte';
import ApiError from '$lib/error/apiError';

export const client = createClient<paths>({
	baseUrl: import.meta.env.VITE_API_URL || 'http://localhost:8080'
});

client.use({
	onRequest: ({ request }) => {
		const token = authStore.getToken();

		if (!token) {
			throw new ApiError(401, 'No bearer token');
		}

		request.headers.set('Authorization', `Bearer ${token}`);
		return request;
	}
});

export type { components, paths } from '$lib/api/schema';
