import type { UUID } from 'crypto';
import { request } from '$lib/api/client';
import type { Room } from '$lib/types/room';
import type { CreateWorkspaceRequest, Invite, Workspace } from '$lib/types';

export const workspaceApi = {
	getRooms: (workspaceId: UUID): Promise<Room[]> => request(`/workspaces/${workspaceId}/rooms`),
	getInvite: (workspaceId: UUID): Promise<Invite> => request(`/workspaces/${workspaceId}/invite`),
	createWorkspace: (createWorkspaceRequest: CreateWorkspaceRequest): Promise<Workspace> =>
		request('/workspaces', { method: 'POST', body: createWorkspaceRequest })
};
