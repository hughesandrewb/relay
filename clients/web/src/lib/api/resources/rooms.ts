import type { UUID } from 'crypto';
import { client, type components } from '$lib/api/client';
import type { CreateMessageRequest, MessageDto } from '$lib/api/resources/messages';
import { handleResponse } from '$lib/api/utils/errors';
import { createMessage, createRoom, type Message, type Room } from '$lib/models';

export type RoomDto = components['schemas']['RoomDto'];
export type CreateRoomRequest = components['schemas']['CreateRoomRequest'];
export type UpdateRoomRequest = components['schemas']['UpdateRoomRequest'];

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
	updateRoom: async (roomId: UUID, update: UpdateRoomRequest): Promise<Room> => {
		const dto: RoomDto = await handleResponse(
			client.PATCH('/api/rooms/{room-id}', {
				params: {
					path: {
						'room-id': roomId
					}
				},
				body: update
			})
		);
		return createRoom(dto);
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
	},
	createRoom: async (workspaceId: UUID, room: CreateRoomRequest): Promise<Room> => {
		const dto: RoomDto = await handleResponse(
			client.POST('/api/workspaces/{workspace-id}/rooms', {
				params: {
					path: {
						'workspace-id': workspaceId
					}
				},
				body: room
			})
		);

		return createRoom(dto);
	}
};
