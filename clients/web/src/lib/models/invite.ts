import type { InviteDto } from '$lib/api/resources/invites';
import type { UUID } from 'crypto';

export interface Invite {
	code: string;
	sender?: {
		id: UUID;
		displayName: string;
	};
	workspace?: {
		id: UUID;
		name: string;
	};
	expiresAt: Date;
	createdAt: Date;
}

export function createInvite(dto: InviteDto): Invite {
	return {
		code: dto.code!,
		sender: dto.sender
			? {
					id: dto.sender.id! as UUID,
					displayName: dto.sender.displayName!
				}
			: undefined,
		workspace: dto.workspace
			? {
					id: dto.workspace.id! as UUID,
					name: dto.workspace.name!
				}
			: undefined,
		expiresAt: new Date(dto.expiresAt!),
		createdAt: new Date(dto.createdAt!)
	};
}
