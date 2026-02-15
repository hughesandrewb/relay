import { profileApi } from '$lib/api/profiles';
import type { Profile } from '$lib/types';

class ProfileStore {
	currentProfile: Profile | undefined;
	isLoading = $state(false);

	async getCurrentProfile() {
		this.isLoading = true;

		try {
			this.currentProfile = await profileApi.getProfile();
		} finally {
			this.isLoading = false;
		}
	}
}

export const profileStore = new ProfileStore();
