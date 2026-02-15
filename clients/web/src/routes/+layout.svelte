<script lang="ts">
	import './layout.css';
	import favicon from '$lib/assets/favicon.svg';
	import { goto } from '$app/navigation';
	import { authStore } from '$lib/stores/auth.svelte';
	import { profileStore } from '$lib/stores/profile.svelte';

	let { children } = $props();

	$effect(() => {
		async function setup() {
			await authStore.init();
			profileStore.getCurrentProfile();

			if (!authStore.isAuthenticated) {
				goto('/login');
			}
		}
		setup();
	});
</script>

<svelte:head><link rel="icon" href={favicon} /></svelte:head>
{@render children()}
