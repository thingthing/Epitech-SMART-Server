package eip.smart.model.proxy;

import eip.smart.model.Agent;
import eip.smart.model.Agent.AgentType;

public class SimpleAgentProxy extends Proxy<Agent> {

	public SimpleAgentProxy(Agent object) {
		super(object);
	}

	public int getID() {
		return (this.object.getID());
	}

	public AgentType getType() {
		return (this.object.getType());
	}
}