<script lang="ts">
	import { profileStore } from '$lib/stores/profile.svelte';

	$effect(() => {
		profileStore.getFriends();
	});
</script>

<div class="flex h-full w-60 flex-col gap-2 border-r p-2">
	<div class="text-2xl font-medium">Your Friends</div>
	<hr />
	{#each profileStore.friends as friend}
		<button
			class="flex w-full cursor-pointer flex-row items-center gap-4 rounded-xl p-2 hover:bg-black/10"
			onclick={() => console.log('select friend')}
		>
			<div class="relative">
				<div class="size-12 rounded-full" style="background-color: #{friend.accentColor};"></div>
				<div
					class="absolute top-0 right-0 flex size-4.5 items-center justify-center rounded-full bg-white outline -outline-offset-3"
				>
					<div class="size-2.25 rounded-full bg-yellow-500"></div>
				</div>
			</div>
			<div>
				<div>{friend.username}</div>
			</div>
		</button>
	{/each}
</div>
