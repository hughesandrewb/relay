<script lang="ts">
	import { Plus, UsersRound } from '@lucide/svelte';
	import { page } from '$app/state';
	import { workspaceStore } from '$lib/stores/workspace.svelte';
	import { modalStore } from '$lib/stores/modal.svelte';
	import CreateWorkspace from '$lib/components/workspace/CreateWorkspace.svelte';
	import Logo from '$lib/components/Logo.svelte';

	const currentWorkspaceId: string | undefined = $derived(page.params.workspaceId);

	$effect(() => {
		workspaceStore.getWorkspaces();
	});

	function showCreateWorkspaceModal() {
		modalStore.openModal(CreateWorkspace);
	}
</script>

<div class="flex h-full w-20 flex-col justify-between border-r">
	<div class="flex h-full flex-col items-center gap-2">
		<button class="flex cursor-pointer">
			<Logo size={80} />
		</button>
		{#if !workspaceStore.isLoading}
			<div class="my-2 flex flex-col">
				{#each Array.from(workspaceStore.workspaces) as workspace (workspace.id)}
					<div class="group relative flex flex-row items-center justify-around gap-1">
						<div
							class="h-0 w-1 rounded-full bg-black transition-all duration-150 {currentWorkspaceId ===
								workspace.id && 'h-6'}"
						></div>
						<a
							class="flex size-16 cursor-pointer items-center justify-center"
							href="/rooms/{workspace.id}"
						>
							<UsersRound size="2.5rem" />
						</a>
						<div class="size-1 rounded-full bg-black {true && 'invisible'}"></div>
						<div
							class="absolute left-full hidden rounded-xl border bg-white p-2 whitespace-nowrap shadow-lg group-hover:block"
						>
							{workspace.name}
						</div>
					</div>
				{/each}
			</div>
			<button
				class="group relative flex size-8 cursor-pointer items-center justify-center rounded-full border-3 border-black hover:bg-black hover:text-white"
				title="Create workspace"
				onclick={showCreateWorkspaceModal}
			>
				<Plus size="2rem" />
				<div
					class="absolute left-12 hidden rounded-xl border bg-white p-2 whitespace-nowrap text-black shadow-lg group-hover:block"
				>
					Create workspace
				</div>
			</button>
		{/if}
	</div>
</div>
