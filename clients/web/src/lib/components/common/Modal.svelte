<script lang="ts">
	import { modalStore } from '$lib/stores/modal.svelte';
	import { clickOutside } from '$lib/utils';
	import { X } from '@lucide/svelte';
	import { onMount } from 'svelte';

	let { children } = $props();

	let dialog: HTMLDialogElement;

	onMount(() => {
		dialog.showModal();
	});

	function closeModal() {
		modalStore.closeModal();
	}
</script>

<dialog
	class="backdrop:black/10 w-lg self-center justify-self-center rounded-xl border-0 p-2 shadow-2xl"
	bind:this={dialog}
>
	<div class="relative p-4" use:clickOutside={closeModal}>
		<button class="absolute top-5 right-5 cursor-pointer" onclick={closeModal}>
			<X />
		</button>
		{@render children?.()}
	</div>
</dialog>
