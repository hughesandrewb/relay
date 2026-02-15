<script lang="ts">
	import { page } from '$app/state';
	import { messageStore } from '$lib/stores/message.svelte';
	import { profileStore } from '$lib/stores/profile.svelte';
	import type { Message } from '$lib/types';
	import type { UUID } from 'crypto';

	const currentRoomId: UUID | undefined = $derived(page.params.roomId as UUID);

	let messageInput: string = $state('');

	let messages: Message[] | undefined = $derived(messageStore.messagesByRoomId.get(currentRoomId));

	function handleKeydown(event: KeyboardEvent) {
		if (event.key !== 'Enter' || messageInput.length === 0) return;
		messageStore.sendMessage(messageInput);
		messageInput = '';
	}

	$effect(() => {
		if (!currentRoomId) {
			return;
		}
		messageStore.getMessages(currentRoomId);
	});
</script>

<div class="flex h-full w-full flex-col bg-white">
	{#if currentRoomId}
		<div class="grid grow content-end gap-1 px-4">
			<span class="justify-self-center text-gray-400">Beginning of chat</span>
			{#each messages as message (message.id)}
				<div
					class="flex {message.authorId === profileStore.currentProfile?.id
						? 'justify-self-end'
						: 'justify-self-start'} gap-2"
				>
					{#if message.authorId !== profileStore.currentProfile?.id}
						<div class="h-8 w-8 rounded-xl bg-black"></div>
					{/if}
					<span
						class="{message.authorId === profileStore.currentProfile?.id
							? 'bg-gray-200'
							: 'bg-gray-300'} rounded-xl px-2 py-1">{message.content}</span
					>
				</div>
			{/each}
		</div>
		<div class="m-4 flex pt-2">
			<!-- svelte-ignore a11y_autofocus -->
			<input
				type="text"
				class="mr-4 w-full rounded-xl border-2 border-gray-500 p-2 focus:outline-0"
				autofocus
				placeholder="Message"
				bind:value={messageInput}
				onkeydown={handleKeydown}
			/>
		</div>
	{:else}
		<div class="flex h-full w-full items-center justify-center">
			<span>No rooms</span>
		</div>
	{/if}
</div>

<style>
</style>
