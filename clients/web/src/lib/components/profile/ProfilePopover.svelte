<script lang="ts">
	import { profileStore } from '$lib/stores/profile.svelte';
</script>

<div class="absolute bottom-4 left-4 rounded border bg-white p-2">
	{#if !profileStore.isLoading && profileStore.currentProfile}
		<div class="flex flex-row items-center gap-4">
			<div class="relative">
				<div
					class="size-20 rounded-full"
					style="background-color: #{profileStore.currentProfile.accentColor};"
				></div>
				<div
					class="absolute top-0 right-0 flex size-7.5 items-center justify-center rounded-full bg-white outline -outline-offset-4"
				>
					<div class="size-4.5 rounded-full bg-green-500"></div>
				</div>
			</div>
			<div>
				<div>{profileStore.currentProfile.displayName}</div>
				<div>@{profileStore.currentProfile.username}</div>
			</div>
		</div>
	{/if}
</div>
