import { profileApi } from '$lib/api/resources/profiles';
import type { Friend, Profile } from '$lib/models';

class ProfileStore {
	currentProfile: Profile | undefined = $state();
	friends: Friend[] | undefined = $state();
	isLoading = $state(false);

	async getCurrentProfile() {
		this.isLoading = true;

		try {
			this.currentProfile = await profileApi.getProfile();
		} finally {
			this.isLoading = false;
		}
	}

	async getFriends() {
		this.isLoading = true;

		try {
			this.friends = await profileApi.getFriends();
		} finally {
			this.isLoading = false;
		}
	}
}

export const profileStore = new ProfileStore();
