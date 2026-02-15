import { profileApi } from '$lib/api/profiles';
import type { Workspace } from '$lib/types';
import { SvelteMap } from 'svelte/reactivity';

class WorkspaceStore {
	workspacesById = new SvelteMap<string, Workspace>();
	workspaces: Workspace[] = $derived(Array.from(this.workspacesById.values()));
	isLoading = $state(false);

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
}

export const workspaceStore = new WorkspaceStore();
