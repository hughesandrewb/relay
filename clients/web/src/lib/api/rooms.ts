import type { CreateMessageRequest, Message } from '$lib/types';
import type { UUID } from 'crypto';
import { request } from './client';

export const roomApi = {
	getMessages: (roomId: UUID): Promise<Message[]> => request(`/rooms/${roomId}/messages`),
	sendMessage: (roomId: UUID, payload: CreateMessageRequest): Promise<Message> =>
		request(`/rooms/${roomId}/messages`, { method: 'POST', body: payload })
};
