import type { UUID } from 'crypto';

export type Message = {
	id: number; // snowflake
	authorId: UUID;
	roomId: UUID;
	content: string;
};
