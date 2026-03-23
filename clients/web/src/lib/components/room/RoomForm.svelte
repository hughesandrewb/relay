
<script lang="ts">
	import { roomApi, type UpdateRoomRequest } from '$lib/api/resources/rooms';
	import type { Room } from '$lib/models';
	import { modalStore } from '$lib/stores/modal.svelte';
	import { profileStore } from '$lib/stores/profile.svelte';
	import { roomStore } from '$lib/stores/room.svelte';
	import { BadgePlus, Settings } from '@lucide/svelte';

	type Props = {
		existingRoom: Room | undefined;
	};

	type RoomForm = {
		name: string;
	};

	let { existingRoom }: Props = $props();

	let isEditing: boolean = $derived(!!existingRoom);

	let room: RoomForm = $state({
		name: existingRoom?.name || `${profileStore.currentProfile?.username}'s room`
	});

	async function createRoom() {
		// const createRoomRequest: CreateRoomRequest = {
		// 	name: room.name
		// };

		// const createdRoom = await roomApi.createRoom(createRoomRequest);

		// roomStore.addRoom(createdRoom);
		// modalStore.closeModal();
	}

	async function updateRoom() {
		if (!existingRoom) {
			return;
		}

		const updateRoomRequest: UpdateRoomRequest = {
			name: room.name
		};

		const updatedRoom: Room = await roomApi.updateRoom(
			existingRoom.id,
			updateRoomRequest
		);

		roomStore.updateRoom(updatedRoom);
		modalStore.closeModal();
	}
</script>

<div class="flex flex-col gap-4">
	{#if isEditing}
		<div>
			<div class="flex flex-row items-center gap-2 p-2 text-3xl">
				<Settings size="2rem" />
				<div>Edit room!</div>
			</div>
		</div>
	{:else}
		<div>
			<div class="flex flex-row items-center gap-2 p-2 text-3xl">
				<BadgePlus size="2rem" />
				<div>Create a room!</div>
			</div>
		</div>
	{/if}
	<div>
		<label for="roomName" class="mb-2 block text-sm font-bold text-gray-700"
			>Room Name</label
		>
		<input
			class="focus:shadow-outline w-full appearance-none rounded border px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
			id="roomName"
			type="text"
			bind:value={room.name}
		/>
	</div>
	<div class="flex items-center justify-end">
		{#if isEditing}
			<button
				class="cursor-pointer rounded border-2 px-4 py-2 font-bold hover:bg-black/10"
				type="button"
				onclick={updateRoom}
			>
				Update
			</button>
		{:else}
			<button
				class="cursor-pointer rounded border-2 px-4 py-2 font-bold hover:bg-black/10"
				type="button"
				onclick={createRoom}
			>
				Create
			</button>
		{/if}
	</div>
</div>
