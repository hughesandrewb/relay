<script lang="ts">
	import { workspaceApi } from '$lib/api/resources/workspaces';
	import type { Workspace } from '$lib/models';
	import type { Invite } from '$lib/models/invite';
	import { Loader, UserRoundPlus } from '@lucide/svelte';
	import { PUBLIC_BASE_URL } from '$env/static/public';

	type Props = {
		workspace: Workspace;
	};

	const { workspace }: Props = $props();

	let loading: boolean = $state(false);
	let invite: Invite | undefined = $state();
	let inviteUrl: string = $derived(`${PUBLIC_BASE_URL}/invites/${invite ? invite.code : ''}`);

	let copied = $state(false);
	let copyAnimationTimeout: ReturnType<typeof setTimeout>;

	$effect(() => {
		async function retrieveInvite() {
			loading = true;
			try {
				invite = await workspaceApi.getInvite(workspace.id);
			} catch (e) {
				// TODO: handle error
				console.log(e);
			} finally {
				loading = false;
			}
		}

		retrieveInvite();
	});

	function handleInviteUrlCopy() {
		navigator.clipboard.writeText(inviteUrl);

		copied = true;
		clearTimeout(copyAnimationTimeout);
		copyAnimationTimeout = setTimeout(() => {
			copied = false;
		}, 2000);
	}
</script>

<div>
	<div class="flex flex-row gap-2 p-2 text-2xl">
		<UserRoundPlus size="2rem" />
		<div>Invite friends to {workspace.name}!</div>
	</div>
	<div class="flex flex-col gap-4 p-2">
		<div>Send this link to your friends to invite them to this workspace!</div>

		{#if !loading}
			<div class="flex flex-row items-center justify-between rounded-xl bg-black/10 p-2">
				<span class="text-lg">{inviteUrl}</span>
				<button
					id="copyBtn"
					onclick={() => handleInviteUrlCopy()}
					class="flex items-center gap-2 rounded-lg border border-zinc-700 bg-zinc-800 px-4 py-2 text-sm font-medium text-zinc-200 transition-all duration-150 hover:border-zinc-500 hover:bg-zinc-700 active:scale-95"
				>
					{#if !copied}
						<!-- Copy icon -->
						<svg
							id="copyIcon"
							xmlns="http://www.w3.org/2000/svg"
							class="h-4 w-4"
							fill="none"
							viewBox="0 0 24 24"
							stroke="currentColor"
							stroke-width="2"
						>
							<rect x="9" y="9" width="13" height="13" rx="2" ry="2"></rect>
							<path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"></path>
						</svg>
					{:else}
						<!-- Check icon -->
						<svg
							id="checkIcon"
							xmlns="http://www.w3.org/2000/svg"
							class="h-4 w-4 text-emerald-400"
							fill="none"
							viewBox="0 0 24 24"
							stroke="currentColor"
							stroke-width="2.5"
						>
							<polyline points="20 6 9 17 4 12"></polyline>
						</svg>
					{/if}
					<span id="btnLabel">{copied ? 'Copied' : 'Copy'}</span>
				</button>
			</div>
		{:else}
			<div class="animate-spin self-center" style="animation-duration: 1750ms;">
				<Loader />
			</div>
		{/if}
	</div>
</div>
