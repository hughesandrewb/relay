<script lang="ts">
	import './layout.css';
	import favicon from '$lib/assets/favicon.svg';
	import { authStore } from '$lib/stores/auth.svelte';
	import { profileStore } from '$lib/stores/profile.svelte';
	import { page } from '$app/state';
	import { wsStore } from '$lib/stores/websocket.svelte';

	let { children } = $props();

	const redirect = $derived(page.url.href);

	$effect(() => {
		async function setup() {
			await authStore.init();
			profileStore.getCurrentProfile();

			if (!authStore.isAuthenticated) {
				authStore.login(redirect);
			}

			const wsUrl = 'ws://localhost:8080/ws';
			await wsStore.connect(wsUrl);
			wsStore.onAny((message) => {
				console.log('Received from WebSocket:', message);
			});
		}
		setup();
	});
</script>

<svelte:head><link rel="icon" href={favicon} /></svelte:head>
{@render children()}
