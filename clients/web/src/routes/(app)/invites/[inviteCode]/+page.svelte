<script lang="ts">
	import { page } from '$app/state';
	import type { InviteDetails } from '$lib/types';
	import { inviteApi } from '$lib/api/invites';
	import { authStore } from '$lib/stores/auth.svelte';
	import { goto } from '$app/navigation';

	const inviteCode: string | undefined = $derived(page.params.inviteCode);

	let invite: InviteDetails | undefined = $state();

	$effect(() => {
		if (!inviteCode || !authStore.isAuthenticated) {
			return;
		}

		async function setup() {
			const res: InviteDetails = await inviteApi.getInviteDetails(inviteCode!);

			invite = res;
		}

		setup();
	});

	async function acceptInvite() {
		if (!invite) {
			return;
		}

		await inviteApi.acceptInvite(inviteCode);
		goto(`/rooms/${invite.workspace.id}`);
	}
</script>

<div class="flex h-dvh items-center justify-center bg-black/10">
	{#if invite}
		<div class="flex flex-col items-center gap-4 rounded-xl bg-white p-4 shadow-2xl">
			<div class="text-3xl">
				{invite.sender.username} has invited to {invite.workspace.name}!
			</div>
			<div class="flex flex-col items-center">
				<div class="flex size-32 items-center justify-center rounded-full bg-red-300 text-7xl">
					AW
				</div>
				<div class="text-4xl">{invite.workspace.name}</div>
			</div>
			<button class="w-3/4 cursor-pointer rounded-xl bg-blue-300 p-2 text-xl" onclick={acceptInvite}
				>Accept Invite</button
			>
		</div>
	{/if}
</div>
