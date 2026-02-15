import type { UUID } from 'crypto';
import { client, type components } from '$lib/api/client';
import type { CreateMessageRequest, MessageDto } from '$lib/api/resources/messages';
import { handleResponse } from '$lib/api/utils/errors';
import { createMessage, type Message } from '$lib/models';

export type RoomDto = components['schemas']['RoomDto'];

export const roomApi = {
	getMessages: async (roomId: UUID): Promise<Message[]> => {
		const dto: MessageDto[] = await handleResponse(
			client.GET('/api/rooms/{room-id}/messages', {
				params: {
					path: {
						'room-id': roomId
					}
				}
			})
		);

		return dto.map((message) => createMessage(message));
	},
	sendMessage: async (roomId: UUID, message: CreateMessageRequest): Promise<Message> => {
		const dto: MessageDto = await handleResponse(
			client.POST('/api/rooms/{room-id}/messages', {
				params: {
					path: {
						'room-id': roomId
					}
				},
				body: message
			})
		);

		return createMessage(dto);
	}
};
