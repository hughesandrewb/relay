import { client, type components } from '$lib/api/client';
import { handleResponse } from '$lib/api/utils/errors';
import type { WorkspaceDto } from '$lib/api/resources/workspaces';
import { createProfile, createWorkspace, type Profile, type Workspace } from '$lib/models';

export type ProfileDto = components['schemas']['ProfileDto'];

export const profileApi = {
	getProfile: async (): Promise<Profile> => {
		const dto: ProfileDto = await handleResponse(client.GET('/api/profiles/me'));

		return createProfile(dto);
	},
	getProfileWorkspaces: async (): Promise<Workspace[]> => {
		const dto: WorkspaceDto[] = await handleResponse(client.GET('/api/profiles/me/workspaces'));

		return dto.map((workspace) => createWorkspace(workspace));
	}
};
