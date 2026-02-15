import { handleResponse } from '$lib/api/utils/errors';
import { client, type components } from '$lib/api/client';
import { createInvite, type Invite } from '$lib/models/invite';

export type InviteDto = components['schemas']['InviteDto'];

export const inviteApi = {
	getInvite: async (inviteCode: string): Promise<Invite> => {
		const dto: InviteDto = await handleResponse(
			client.GET('/api/invites/{invite-code}', {
				params: {
					path: { 'invite-code': inviteCode }
				}
			})
		);

		return createInvite(dto);
	},
	acceptInvite: async (inviteCode: string): Promise<Invite> => {
		const dto: InviteDto = await handleResponse(
			client.POST('/api/invites/{invite-code}', {
				params: {
					path: { 'invite-code': inviteCode }
				}
			})
		);

		return createInvite(dto);
	}
};
