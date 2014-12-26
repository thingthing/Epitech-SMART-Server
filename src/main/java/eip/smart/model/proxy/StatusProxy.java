package eip.smart.model.proxy;

import eip.smart.model.Status;

public class StatusProxy extends Proxy<Status> {

	public StatusProxy(Status object) {
		super(object);
	}

	public int getCode() {
		return (this.object.getCode());
	}

	public String getMessage() {
		return (this.object.getMessage());
	}

}
