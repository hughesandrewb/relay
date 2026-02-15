export default class ApiError extends Error {
	status: number | undefined;

	constructor(status: number, message: string) {
		super(message);
		this.status = status;
	}
}
