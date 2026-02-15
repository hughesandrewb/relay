<script lang="ts">
	import { authStore } from '$lib/stores/auth.svelte';
	import RoomList from '$lib/components/RoomList.svelte';
	import WorkspaceList from '$lib/components/workspace/WorkspaceList.svelte';
	import { modalStore } from '$lib/stores/modal.svelte';
	import Modal from '$lib/components/common/Modal.svelte';

	let { children } = $props();
</script>

<div class="flex h-dvh w-dvw overflow-hidden">
	{#if authStore.isAuthenticated}
		<aside>
			<WorkspaceList />
		</aside>
		<aside>
			<RoomList />
		</aside>
		<main class="main-content h-full w-full">
			{@render children()}
		</main>
	{:else}
		{@render children()}
	{/if}
	{#if modalStore.modal.component}
		<Modal>
			<modalStore.modal.component {...modalStore.modal.props} />
		</Modal>
	{/if}
</div>
