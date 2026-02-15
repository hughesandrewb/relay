import type AuthProvider from '$lib/auth/authProvider.interface';
import KeycloakAuthProvider from '$lib/auth/keycloakAuthProvider';

class AuthStore {
	authProvider: AuthProvider;

	isAuthenticated: boolean = $state(false);
	isLoading: boolean = $state(false);
	error: string | null = $state(null);

	constructor(authProvider: AuthProvider) {
		this.authProvider = authProvider;
	}

	async init() {
		this.isLoading = true;
		const authenticated: boolean = await this.authProvider.init();

		this.isAuthenticated = authenticated;
		this.isLoading = false;
	}

	async login() {
		try {
			this.authProvider.login();

			this.isAuthenticated = this.authProvider.isAuthenticated();
		} catch (error) {
			this.error = error instanceof Error ? error.message : 'Failed to login';
		} finally {
			this.isLoading = false;
		}
	}

	async logout() {
		try {
			this.authProvider.logout();

			this.isAuthenticated = this.authProvider.isAuthenticated();
		} catch (error) {
			this.error = error instanceof Error ? error.message : 'Failed to logout';
		} finally {
			this.isLoading = false;
		}
	}

	getToken(): string {
		return this.authProvider.getToken() ?? '';
	}
}

export const authStore = new AuthStore(new KeycloakAuthProvider());
