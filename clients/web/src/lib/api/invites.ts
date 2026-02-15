import type { InviteDetails } from '$lib/types';
import { request } from '$lib/api/client';

export const inviteApi = {
	getInviteDetails: (code: string): Promise<InviteDetails> => request(`/invites/${code}`),
	acceptInvite: (code: string): Promise<InviteDetails> =>
		request(`/invites/${code}`, { method: 'POST' })
};
