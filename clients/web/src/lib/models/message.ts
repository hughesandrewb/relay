import type { MessageDto } from '$lib/api/resources/messages';
import type { UUID } from 'crypto';

export interface Message {
	id: number;
	authorId: UUID;
	roomId: UUID;
	content: string;
}

export function createMessage(dto: MessageDto): Message {
	return {
		id: dto.id!,
		authorId: dto.authorId! as UUID,
		roomId: dto.roomId! as UUID,
		content: dto.content!
	};
}
