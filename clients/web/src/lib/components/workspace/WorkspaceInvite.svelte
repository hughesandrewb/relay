<script lang="ts">
	import { workspaceApi } from '$lib/api/workspaces';
	import type { Invite, Workspace } from '$lib/types';
	import { UserRoundPlus } from '@lucide/svelte';

	type Props = {
		workspace: Workspace;
	};

	const { workspace }: Props = $props();

	let loading: boolean = $state(false);
	let invite: Invite | null = $state(null);

	$effect(() => {
		async function retrieveInvite() {
			loading = true;
			invite = await workspaceApi.getInvite(workspace.id);
			loading = false;
		}

		retrieveInvite();
	});
</script>

<div>
	<div class="flex flex-row gap-2 p-2 text-2xl">
		<UserRoundPlus size="2rem" />
		<div>Invite friends to {workspace.name}!</div>
	</div>
	<div class="flex flex-col gap-4 p-2">
		<div>Send this link to your friends to invite them to this workspace!</div>
		<div class="flex flex-row items-center justify-between rounded-xl bg-black/10 p-2">
			<span class="text-lg">https://relay.andhug.com/invites/{invite?.code}</span>
			<button class="cursor-pointer rounded-xl bg-blue-300 px-4 py-2">Copy</button>
		</div>
	</div>
</div>
