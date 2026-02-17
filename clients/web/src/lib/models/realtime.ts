import type { RealtimeTicketDto } from '$lib/api/resources/realtime';

export interface RealtimeTicket {
	code: string;
}

export function createRealtimeTicket(dto: RealtimeTicketDto) {
	return {
		code: dto.code!
	};
}
