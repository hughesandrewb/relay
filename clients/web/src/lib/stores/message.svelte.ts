import { page } from '$app/state';
import { roomApi } from '$lib/api/resources/rooms';
import { createMessage, type Message } from '$lib/models';
import type { UUID } from 'crypto';
import { SvelteMap } from 'svelte/reactivity';
import { wsStore } from './websocket.svelte';
import type { MessageDto } from '$lib/api/resources/messages';
import { profileStore } from './profile.svelte';

class MessageStore {
	messagesByRoomId = new SvelteMap<UUID, Message[]>();

	constructor() {
		wsStore.on('DISPATCH', (data: MessageDto) => {
			const message: Message = createMessage(data);
			if (message.authorId === profileStore.currentProfile?.id) {
				return;
			}
			this.addMessage(message);
		});
	}

	async getMessages(roomId: UUID) {
		this.messagesByRoomId.set(roomId, []);
		(await roomApi.getMessages(roomId)).map((message) => this.addMessage(message));
	}

	async sendMessage(content: string) {
		const currentRoomId: UUID | undefined = page.params.roomId as UUID;

		const message: Message = await roomApi.sendMessage(currentRoomId, { content });

		this.addMessage(message);
	}

	private addMessage(message: Message) {
		const currentMessages: Message[] = this.messagesByRoomId.get(message.roomId) ?? [];

		this.messagesByRoomId.set(message.roomId, [...currentMessages, message]);
	}
}

export const messageStore = new MessageStore();
