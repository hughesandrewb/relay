import { workspaceApi } from '$lib/api/resources/workspaces';
import type { Room } from '$lib/models';
import type { UUID } from 'crypto';
import { SvelteMap } from 'svelte/reactivity';

class RoomStore {
	roomsByWorkspaceId = new SvelteMap<UUID, Room[]>();
	roomsById = new SvelteMap<UUID, Room>();
	isLoading = $state(false);

	async getRooms(workspaceId: UUID) {
		this.isLoading = true;

		if (this.roomsByWorkspaceId.has(workspaceId)) {
			return;
		}

		try {
			(await workspaceApi.getRooms(workspaceId)).forEach((room) => this.addRoom(room));
		} finally {
			this.isLoading = false;
		}
	}

	private addRoom(room: Room) {
		this.roomsById.set(room.id, room);

		const currentRooms: Room[] = this.roomsByWorkspaceId.get(room.workspaceId) ?? [];
		this.roomsByWorkspaceId.set(room.workspaceId, [...currentRooms, room]);
	}
}

export const roomStore = new RoomStore();
