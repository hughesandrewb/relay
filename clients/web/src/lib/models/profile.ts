import type { ProfileDto } from '$lib/api/resources/profiles';
import type { UUID } from 'crypto';

export interface Profile {
	id: UUID;
	username: string;
	displayName: string;
	accentColor: string;
}

export function createProfile(dto: ProfileDto): Profile {
	return {
		id: dto.id! as UUID,
		username: dto.username!,
		displayName: dto.displayName!,
		accentColor: dto.accentColor!
	};
}
