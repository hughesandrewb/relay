<script lang="ts">
	import { authStore } from '$lib/stores/auth.svelte';
	import RoomList from '$lib/components/RoomList.svelte';
	import WorkspaceList from '$lib/components/workspace/WorkspaceList.svelte';
	import { modalStore } from '$lib/stores/modal.svelte';
	import Modal from '$lib/components/common/Modal.svelte';
	import ProfilePopover from '$lib/components/profile/ProfilePopover.svelte';
	import { page } from '$app/state';

	let { children } = $props();

	let workspaceId: string | undefined = $derived(page.params.workspaceId);
</script>

<div class="flex h-dvh w-dvw overflow-hidden">
	{#if authStore.isAuthenticated}
		<aside>
			<WorkspaceList />
		</aside>
		{#if workspaceId}
			<aside>
				<RoomList />
			</aside>
		{/if}
		<ProfilePopover />
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
