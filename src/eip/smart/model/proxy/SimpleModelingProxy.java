package eip.smart.model.proxy;

import eip.smart.model.Modeling;

public class SimpleModelingProxy extends Proxy<Modeling> {

	public SimpleModelingProxy(Modeling object) {
		super(object);
	}

	public double getCompletion() {
		return (this.object.getCompletion());
	}

	public String getName() {
		return (this.object.getName());
	}

	public long getTick() {
		return (this.object.getTick());
	}
}
