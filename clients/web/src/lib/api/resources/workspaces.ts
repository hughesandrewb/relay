import type { UUID } from 'crypto';
import { client, type components } from '$lib/api/client';
import type { RoomDto } from '$lib/api/resources/rooms';
import type { InviteDto } from '$lib/api/resources/invites';
import { handleResponse } from '../utils/errors';
import { createRoom, createWorkspace, type Room, type Workspace } from '$lib/models';
import { createInvite, type Invite } from '$lib/models/invite';

export type WorkspaceDto = components['schemas']['WorkspaceDto'];
export type CreateWorkspaceRequest = components['schemas']['CreateWorkspaceRequest'];

export const workspaceApi = {
	getRooms: async (workspaceId: UUID): Promise<Room[]> => {
		const dto: RoomDto[] = await handleResponse(
			client.GET('/api/workspaces/{workspace-id}/rooms', {
				params: {
					path: { 'workspace-id': workspaceId }
				}
			})
		);

		return dto.map((room) => createRoom(room));
	},
	getInvite: async (workspaceId: UUID): Promise<Invite> => {
		const dto: InviteDto = await handleResponse(
			client.GET('/api/workspaces/{workspace-id}/invite', {
				params: {
					path: { 'workspace-id': workspaceId }
				}
			})
		);

		return createInvite(dto);
	},
	createWorkspace: async (createWorkspaceRequest: CreateWorkspaceRequest): Promise<Workspace> => {
		const dto: WorkspaceDto = await handleResponse(
			client.POST('/api/workspaces', {
				body: createWorkspaceRequest
			})
		);

		return createWorkspace(dto);
	}
};
