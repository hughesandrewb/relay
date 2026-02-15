import type { RoomDto } from '$lib/api/resources/rooms';
import type { UUID } from 'crypto';

export interface Room {
	id: UUID;
	name: string;
	workspaceId: UUID;
	color: string;
}

export function createRoom(dto: RoomDto): Room {
	return {
		id: dto.id! as UUID,
		name: dto.name!,
		workspaceId: dto.workspaceId! as UUID,
		color: '38f2b7'
	};
}
