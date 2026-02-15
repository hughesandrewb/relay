import { profileApi } from '$lib/api/resources/profiles';
import type { Profile } from '$lib/models';

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
