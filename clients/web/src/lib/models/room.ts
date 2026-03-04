import type { RoomDto } from '$lib/api/resources/rooms';
import type { UUID } from 'crypto';
import type { Profile } from './profile';

export interface Room {
	id: UUID;
	name: string;
	workspaceId: UUID;
	color: string;
	participants: Profile[];
}

export function createRoom(dto: RoomDto): Room {
	return {
		id: dto.id! as UUID,
		name: dto.name!,
		workspaceId: dto.workspaceId! as UUID,
		color: '000000',
		participants: dto.participants! as Profile[]
	};
}
