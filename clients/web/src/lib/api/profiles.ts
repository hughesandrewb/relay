import type { Profile, Workspace } from '$lib/types';
import { request } from './client';

export const profileApi = {
	getProfile: (): Promise<Profile> => request('/profiles/me'),
	getProfileWorkspaces: (): Promise<Workspace[]> => request('/profiles/me/workspaces')
};
