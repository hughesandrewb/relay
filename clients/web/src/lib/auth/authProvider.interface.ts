export default interface AuthProvider {
	init(): Promise<boolean>;
	login(redirectUri?: string): Promise<void>;
	logout(redirectUri?: string): Promise<void>;
	getToken(): string | undefined;
	isAuthenticated(): boolean;
}
