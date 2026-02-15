import type { Profile, Workspace } from '$lib/types';

export type InviteDetails = {
	workspace: Workspace;
	sender: Profile;
};

export type Invite = {
	code: string;
	createdAt: Date;
	expiresAt: Date;
};
