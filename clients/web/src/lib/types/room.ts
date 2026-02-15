import type { UUID } from 'crypto';

export type Room = {
	id: UUID;
	name: string;
	workspaceId: UUID;
	color: string;
};
