<script lang="ts">
	import { workspaceApi } from '$lib/api/workspaces';
	import { modalStore } from '$lib/stores/modal.svelte';
	import { profileStore } from '$lib/stores/profile.svelte';
	import type { CreateWorkspaceRequest } from '$lib/types';
	import { BadgePlus, ImageUp } from '@lucide/svelte';

	let createWorkspaceRequest: CreateWorkspaceRequest = $state({
		name: `${profileStore.currentProfile?.username}'s workspace`
	});

	async function createWorkspace() {
		await workspaceApi.createWorkspace(createWorkspaceRequest);
		modalStore.closeModal();
	}
</script>

<div class="flex flex-col gap-4">
	<div>
		<div class="flex flex-row gap-2 p-2 text-2xl">
			<BadgePlus size="2rem" />
			<div>Create a workspace!</div>
		</div>
		<div class="px-10 text-sm">
			Give your new workspace a personality with a name and an icon. Don't worry, you can change it
			later
		</div>
	</div>
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
			bind:value={createWorkspaceRequest.name}
		/>
	</div>
	<div class="flex items-center justify-end">
		<button
			class="cursor-pointer rounded border-2 px-4 py-2 font-bold hover:bg-black/10"
			type="button"
			onclick={createWorkspace}
		>
			Create
		</button>
	</div>
</div>
