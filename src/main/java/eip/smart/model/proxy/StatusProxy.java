package eip.smart.model.proxy;

import eip.smart.server.servlet.JsonServlet.Status;

public class StatusProxy extends Proxy<Status> {

	public StatusProxy(Status object) {
		super(object);
	}

	public String getMessage() {
		return (this.object.getMessage());
	}

	public int getStatus() {
		return (this.object.getCode());
	}

}
