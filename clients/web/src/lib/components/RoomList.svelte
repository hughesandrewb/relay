<script lang="ts">
	import { page } from '$app/state';
	import { workspaceStore } from '$lib/stores/workspace.svelte';
	import { roomStore } from '$lib/stores/room.svelte';
	import { goto } from '$app/navigation';
	import { ChevronDown, Settings, UserRoundPlus, BadgePlus } from '@lucide/svelte';
	import { clickOutside } from '$lib/utils';
	import type { UUID } from 'crypto';
	import type { Room, Workspace } from '$lib/types';
	import WorkspaceInvite from '$lib/components/workspace/WorkspaceInvite.svelte';
	import { modalStore } from '$lib/stores/modal.svelte';

	const currentWorkspaceId: UUID | undefined = $derived(page.params.workspaceId as UUID);
	const currentRoomId: UUID | undefined = $derived(page.params.roomId as UUID);

	const currentWorkspace: Workspace | undefined = $derived(
		workspaceStore.workspacesById.get(currentWorkspaceId)
	);
	const rooms: Room[] | undefined = $derived(roomStore.roomsByWorkspaceId.get(currentWorkspaceId));

	let showWorkspaceOptions: boolean = $state(false);

	let roomListWidth: number = $state(240);
	let isResizing: boolean = $state(false);

	let startX: number;
	let startWidth: number;

	$effect(() => {
		async function setup() {
			if (!currentWorkspaceId) {
				return;
			}
			await roomStore.getRooms(currentWorkspaceId);
			goto(`/rooms/${currentWorkspaceId}/${rooms![0].id}`);
		}

		setup();
	});

	function toggleShowWorkspaceOptions() {
		showWorkspaceOptions = !showWorkspaceOptions;
	}

	function hideShowWorkspaceOptions() {
		showWorkspaceOptions = false;
	}

	function startResize(event: MouseEvent) {
		isResizing = true;
		startX = event.clientX;
		startWidth = roomListWidth;
	}

	function stopResize() {
		console.log(roomListWidth);
		isResizing = false;
	}

	function resize(event: MouseEvent) {
		if (!isResizing) {
			return;
		}
		const delta: number = event.clientX - startX;
		const newWidth: number = startWidth + delta;

		if (newWidth >= 150 && newWidth <= 600) {
			roomListWidth = newWidth;
		}
	}
</script>

<svelte:window onmouseup={stopResize} onmousemove={resize} />

<aside class="flex h-full flex-row border-r" style="width: {roomListWidth}px;">
	<div class="flex h-full w-full flex-col gap-2 p-2">
		<div class="flex flex-row justify-between">
			<span class="basis-64 text-xl">
				{currentWorkspace?.name}
			</span>
			<div class="relative">
				<button class="basis-8 cursor-pointer" onclick={toggleShowWorkspaceOptions}>
					<ChevronDown />
				</button>
				{#if showWorkspaceOptions}
					<div
						class="absolute flex w-64 flex-col rounded-xl border bg-white text-lg shadow-2xl"
						use:clickOutside={hideShowWorkspaceOptions}
					>
						<button
							class="flex cursor-pointer flex-row gap-2 p-3"
							onclick={() => console.log('Show workspace settings')}
						>
							<Settings />
							<span>Workspace Settings</span>
						</button>
						<button
							class="flex cursor-pointer flex-row gap-2 p-3"
							onclick={() => console.log('Show create room')}
						>
							<BadgePlus />
							<span>Create Room</span>
						</button>
						<button
							class="flex cursor-pointer flex-row gap-2 p-3"
							onclick={() => {
								modalStore.openModal(WorkspaceInvite, { workspace: currentWorkspace! });
								hideShowWorkspaceOptions();
							}}
						>
							<UserRoundPlus />
							<span>Invite to Workspace</span>
						</button>
					</div>
				{/if}
			</div>
		</div>
		<hr class="w-full" />
		{#if !workspaceStore.isLoading}
			<div class="flex h-full flex-col gap-2">
				{#each rooms as room (room.id)}
					<div
						class="flex flex-row items-center gap-2 rounded-xl px-4 py-1 {currentRoomId ===
							room.id && 'bg-black/10'}"
					>
						<span class="text-2xl" style="color: #{room.color ?? '000000'}">#</span>
						<a
							class="cursor-pointer text-xl transition-all duration-150 ease-in-out"
							href="/rooms/{currentWorkspaceId}/{room.id}"
						>
							{room.name}
						</a>
					</div>
				{/each}
			</div>
		{/if}
	</div>
	<button
		class="h-full w-1 cursor-col-resize"
		onmousedown={startResize}
		aria-label="Resize room list sidebar"
	></button>
</aside>
