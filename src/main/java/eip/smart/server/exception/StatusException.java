package eip.smart.server.exception;

import eip.smart.model.Status;

public class StatusException extends Exception {
	private Status	status;

	public StatusException(Status s) {
		this.status = s;
	}

	public Status getStatus() {
		return (this.status);
	}
}
