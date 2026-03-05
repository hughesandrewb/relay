<script lang="ts">
	import {
		workspaceApi,
		type CreateWorkspaceRequest,
		type UpdateWorkspaceRequest
	} from '$lib/api/resources/workspaces';
	import type { Workspace } from '$lib/models';
	import { modalStore } from '$lib/stores/modal.svelte';
	import { profileStore } from '$lib/stores/profile.svelte';
	import { workspaceStore } from '$lib/stores/workspace.svelte';
	import { BadgePlus, ImageUp, Settings } from '@lucide/svelte';

	type Props = {
		existingWorkspace: Workspace | undefined;
	};

	type WorkspaceForm = {
		name: string;
	};

	let { existingWorkspace }: Props = $props();

	let isEditing: boolean = $derived(!!existingWorkspace);

	let workspace: WorkspaceForm = $state({
		name: existingWorkspace?.name || `${profileStore.currentProfile?.username}'s workspace`
	});

	async function createWorkspace() {
		const createWorkspaceRequest: CreateWorkspaceRequest = {
			name: workspace.name
		};

		const createdWorkspace = await workspaceApi.createWorkspace(createWorkspaceRequest);

		workspaceStore.addWorkspace(createdWorkspace);
		modalStore.closeModal();
	}

	async function updateWorkspace() {
		if (!existingWorkspace) {
			return;
		}

		const updateWorkspaceRequest: UpdateWorkspaceRequest = {
			name: workspace.name
		};

		const updatedWorkspace = await workspaceApi.updateWorkspace(
			existingWorkspace.id,
			updateWorkspaceRequest
		);

		workspaceStore.updateWorkspace(updatedWorkspace);
		modalStore.closeModal();
	}
</script>

<div class="flex flex-col gap-4">
	{#if isEditing}
		<div>
			<div class="flex flex-row items-center gap-2 p-2 text-3xl">
				<Settings size="2rem" />
				<div>Edit workspace!</div>
			</div>
		</div>
	{:else}
		<div>
			<div class="flex flex-row items-center gap-2 p-2 text-3xl">
				<BadgePlus size="2rem" />
				<div>Create a workspace!</div>
			</div>
			<div class="px-10 text-sm">
				Give your new workspace a personality with a name and an icon. Don't worry, you can change
				it later
			</div>
		</div>
	{/if}
	<div class="self-center">
		<div
			class="flex size-32 flex-col items-center justify-center rounded-full border-4 border-dashed"
		>
			<ImageUp size="2.5rem" />
			<div class="font-semibold">Upload</div>
		</div>
	</div>
	<div>
		<label for="workspaceName" class="mb-2 block text-sm font-bold text-gray-700"
			>Workspace Name</label
		>
		<input
			class="focus:shadow-outline w-full appearance-none rounded border px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
			id="workspaceName"
			type="text"
			bind:value={workspace.name}
		/>
	</div>
	<div class="flex items-center justify-end">
		{#if isEditing}
			<button
				class="cursor-pointer rounded border-2 px-4 py-2 font-bold hover:bg-black/10"
				type="button"
				onclick={updateWorkspace}
			>
				Update
			</button>
		{:else}
			<button
				class="cursor-pointer rounded border-2 px-4 py-2 font-bold hover:bg-black/10"
				type="button"
				onclick={createWorkspace}
			>
				Create
			</button>
		{/if}
	</div>
</div>
