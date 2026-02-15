import type { WorkspaceDto } from '$lib/api/resources/workspaces';
import { profileStore } from '$lib/stores/profile.svelte';
import type { UUID } from 'crypto';

export interface Workspace {
	id: UUID;
	name: string;
	owner: boolean;
}

export function createWorkspace(dto: WorkspaceDto): Workspace {
	return {
		id: dto.id! as UUID,
		name: dto.name!,
		owner: dto.ownerId! == profileStore.currentProfile?.id
	};
}
