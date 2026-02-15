/* eslint-disable @typescript-eslint/no-explicit-any */
export class ApiError extends Error {
	constructor(
		public status: number,
		message: string,
		public details?: any
	) {
		super(message);
	}
}

export async function handleResponse<T>(promise: Promise<{ data?: T; error?: any }>): Promise<T> {
	const { data, error } = await promise;
	if (error) {
		throw new ApiError(error.status || 500, error.message || 'API Error', error);
	}
	return data!;
}
