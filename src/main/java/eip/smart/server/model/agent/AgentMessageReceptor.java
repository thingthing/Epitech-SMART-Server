package eip.smart.server.model.agent;

import eip.smart.cscommons.model.agent.AgentState;
import eip.smart.cscommons.model.geometry.v2.Point3D;

public enum AgentMessageReceptor {
	POSITION("position", new AgentMessageHandler<Point3D>(Point3D.class) {
		@Override
		public void handleMessage(Point3D data, AgentLogic agent) {
			agent.setCurrentPosition(data);
			agent.sendStatus(0, "ok");
		}
	}),
	STATE("state", new AgentMessageHandler<String>(String.class) {
		@Override
		public void handleMessage(String data, AgentLogic agent) {
			for (AgentState as : AgentState.values())
				if (as.name().equals(data)) {
					agent.setState(as);
					agent.sendStatus(0, "ok");
					return;
				}
			agent.sendStatus(1, "unknown state");
		}
	});

	private String					key;
	private AgentMessageHandler<?>	handler;

	private AgentMessageReceptor(String key, AgentMessageHandler<?> handler) {
		this.key = key;
		this.handler = handler;
	}

	/**
	 * @return the handler
	 */
	public AgentMessageHandler<?> getHandler() {
		return this.handler;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return this.key;
	}

}
