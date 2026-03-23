import { profileApi } from '$lib/api/resources/profiles';
import { createWorkspace, type Workspace } from '$lib/models';
import { SvelteMap } from 'svelte/reactivity';
import { wsStore } from './websocket.svelte';
import type { WorkspaceDto } from '$lib/api/resources/workspaces';

class WorkspaceStore {
	workspacesById = new SvelteMap<string, Workspace>();
	workspaces: Workspace[] = $derived(Array.from(this.workspacesById.values()));
	isLoading = $state(false);

	constructor() {
		wsStore.on('WORKSPACE_UPDATE', (data: WorkspaceDto) => {
			const workspace: Workspace = createWorkspace(data);

			this.updateWorkspace(workspace);
		});
	}

	async getWorkspaces() {
		this.isLoading = true;

		try {
			(await profileApi.getProfileWorkspaces()).forEach((workspace) =>
				this.addWorkspace(workspace)
			);
		} finally {
			this.isLoading = false;
		}
	}

	addWorkspace(workspace: Workspace) {
		this.workspacesById.set(workspace.id, workspace);
	}

	updateWorkspace(workspace: Workspace) {
		if (!this.workspacesById.has(workspace.id)) {
			return;
		}
		this.workspacesById.set(workspace.id, workspace);
	}
}

export const workspaceStore = new WorkspaceStore();
