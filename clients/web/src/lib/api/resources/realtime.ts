import { client, type components } from '$lib/api/client';
import { createRealtimeTicket } from '$lib/models/realtime';
import { handleResponse } from '../utils/errors';

export type RealtimeTicketDto = components['schemas']['RealtimeTicketDto'];

export const realtimeApi = {
	getRealtimeTicket: async () => {
		const dto: RealtimeTicketDto = await handleResponse(client.GET('/api/realtime/ticket'));

		return createRealtimeTicket(dto);
	}
};
