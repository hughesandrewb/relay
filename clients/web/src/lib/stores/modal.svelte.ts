import type { Component } from 'svelte';

type Modal = {
	component: Component | null;
	header: string | null;
	icon: Component | null;
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	props: Record<string, any>;
};

class ModalStore {
	modal: Modal = $state({
		component: null,
		header: null,
		icon: null,
		props: {}
	});

	public openModal(component: Component, props = {}) {
		this.modal.component = component;
		this.modal.props = props;
	}

	public closeModal() {
		this.modal.component = null;
		this.modal.props = {};
	}
}

export const modalStore = new ModalStore();
