import type { MessageDto } from '$lib/api/resources/messages';
import type { UUID } from 'crypto';
import { createProfile, type Profile } from '$lib/models';

export interface Message {
	id: number;
	author: Profile;
	roomId: UUID;
	content: string;
}

export function createMessage(dto: MessageDto): Message {
	return {
		id: dto.id!,
		author: createProfile(dto.author!),
		roomId: dto.roomId! as UUID,
		content: dto.content!
	};
}
