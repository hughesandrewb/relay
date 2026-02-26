import type { FriendSummaryDto } from '$lib/api/resources/profiles';
import type { UUID } from 'crypto';

export interface Friend {
	id: UUID;
	username: string;
	accentColor: string;
}

export function createFriend(friendSummaryDto: FriendSummaryDto): Friend {
	return {
		id: friendSummaryDto.id as UUID,
		username: friendSummaryDto.username!,
		accentColor: friendSummaryDto.accentColor!
	};
}
