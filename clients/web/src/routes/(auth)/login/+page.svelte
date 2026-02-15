<script lang="ts">
	import { goto } from '$app/navigation';
	import { authStore } from '$lib/stores/auth.svelte';
	import { onMount } from 'svelte';

	onMount(() => {
		if (authStore.isAuthenticated) {
			goto('/rooms');
		}
	});

	async function handleLogin() {
		await authStore.login();
	}
</script>

<div class="login-container">
	<button onclick={handleLogin}>Sign In with Keycloak</button>
</div>
