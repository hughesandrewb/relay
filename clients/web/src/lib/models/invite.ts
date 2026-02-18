import type { InviteDto } from '$lib/api/resources/invites';
import { createProfile, createWorkspace, type Profile, type Workspace } from '$lib/models';

export interface Invite {
	code: string;
	sender?: Profile;
	workspace?: Workspace;
	expiresAt: Date;
	createdAt: Date;
}

export function createInvite(dto: InviteDto): Invite {
	return {
		code: dto.code!,
		sender: dto.sender ? createProfile(dto.sender) : undefined,
		workspace: dto.workspace ? createWorkspace(dto.workspace) : undefined,
		expiresAt: new Date(dto.expiresAt!),
		createdAt: new Date(dto.createdAt!)
	};
}
