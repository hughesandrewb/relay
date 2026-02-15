import type { UUID } from 'crypto';

export type Profile = {
	id: UUID;
	username: string;
	displayName: string;
};
