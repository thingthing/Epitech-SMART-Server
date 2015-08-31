package eip.smart.server.exception;

import eip.smart.cscommons.model.ServerStatus;

public class StatusException extends Exception {
	private ServerStatus	status;

	public StatusException(ServerStatus s) {
		this.status = s;
	}

	public ServerStatus getStatus() {
		return (this.status);
	}
}
