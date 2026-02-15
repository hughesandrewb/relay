import type { UUID } from 'crypto';

export type Workspace = {
	id: UUID;
	name: string;
	owner: boolean;
};

export type CreateWorkspaceRequest = {
	name: string;
};
