import Keycloak from 'keycloak-js';
import type AuthProvider from './authProvider.interface';

class KeycloakAuthProvider implements AuthProvider {
	private keycloak: Keycloak;
	private authChangeCallbacks: ((authenticated: boolean) => void)[] = [];
	private initPromise: Promise<boolean> | null = null;

	constructor() {
		this.keycloak = new Keycloak({
			url: 'http://localhost:8081',
			realm: 'relay',
			clientId: 'web'
		});
	}

	async init(): Promise<boolean> {
		if (!this.initPromise) {
			this.initPromise = this._init();
		}

		return this.initPromise;
	}

	private async _init(): Promise<boolean> {
		const authenticated: boolean = await this.keycloak.init({
			onLoad: 'check-sso',
			checkLoginIframe: false,
			silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
			pkceMethod: 'S256'
		});

		this.notifyAuthChange(authenticated);

		return authenticated;
	}

	async login(redirectUri?: string): Promise<void> {
		await this.keycloak.login({
			redirectUri: redirectUri || window.location.origin
		});
	}

	async logout(redirectUri?: string): Promise<void> {
		await this.keycloak.logout({
			redirectUri: redirectUri || window.location.origin
		});
	}

	getToken(): string | undefined {
		return this.keycloak.token;
	}

	isAuthenticated(): boolean {
		return this.keycloak.authenticated;
	}

	private notifyAuthChange(authenticated: boolean) {
		this.authChangeCallbacks.forEach((callback) => callback(authenticated));
	}
}

export default KeycloakAuthProvider;
