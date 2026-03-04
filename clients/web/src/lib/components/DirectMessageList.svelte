<script lang="ts">
	import { profileStore } from '$lib/stores/profile.svelte';
	import { roomStore } from '$lib/stores/room.svelte';

	$effect(() => {
		profileStore.getFriends();
		roomStore.getDirectMessages();
	});
</script>

<div class="flex h-full w-60 flex-col divide-y border-r">
	<div class="p-2">
		<div class="text-2xl font-medium">Direct Messages</div>
	</div>
	<div class="p-2">
		{#each roomStore.directMessageRooms.values() as dm}
			<a
				class="flex h-16 w-full cursor-pointer flex-row items-center gap-4 rounded p-2 hover:bg-black/10"
				href="/rooms/me/{dm.id}"
			>
				<div class="relative">
					<div
						class="size-12 rounded-full"
						style="background-color: #{dm.participants[0]?.accentColor};"
					></div>
					<div
						class="absolute top-0 right-0 flex size-4.5 items-center justify-center rounded-full bg-white outline -outline-offset-3"
					>
						<div class="size-2.25 rounded-full bg-yellow-500"></div>
					</div>
				</div>
				<div class="flex flex-col items-start text-start">
					<div class="text-lg font-medium">{dm.participants[0]?.username}</div>
					<div class="line-clamp-2 overflow-clip text-sm text-black/60">
						Lorem ipsum dolor, sit amet consectetur adipisicing elit. Veniam facilis quia nihil iste
						saepe quis aut aperiam, in, veritatis reiciendis quasi! Iste odio laborum aliquam ut
						animi iusto, quibusdam fuga?
					</div>
				</div>
			</a>
		{/each}
	</div>
</div>
