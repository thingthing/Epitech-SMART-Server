package eip.smart.server.net.tcp;

import eip.smart.cscommons.model.agent.AgentState;
import eip.smart.cscommons.model.geometry.Point3D;
import eip.smart.server.model.agent.AgentLogic;

public enum AgentCommandList {
	BATTERY("battery", new AgentCommandHandler<Double>(Double.class) {
		@Override
		public void handleCommand(Double battery, AgentLogic agent) throws CommandException {
			agent.setBattery(battery);
		}
	}),
	POSITION("position", new AgentCommandHandler<Point3D>(Point3D.class) {
		@Override
		public void handleCommand(Point3D point, AgentLogic agent) {
			agent.setCurrentPosition(point);
		}
	}),
	STATE("state", new AgentCommandHandler<String>(String.class) {
		@Override
		public void handleCommand(String state, AgentLogic agent) throws CommandException {
			for (AgentState as : AgentState.values())
				if (as.name().equals(state)) {
					agent.setState(as);
					return;
				}
			throw new CommandException("Unknown state " + state);
		}
	});

	private AgentCommandHandler<?>	handler;
	private String					key;

	private AgentCommandList(String key, AgentCommandHandler<?> handler) {
		this.key = key;
		this.handler = handler;
	}

	/**
	 * @return the handler
	 */
	public AgentCommandHandler<?> getHandler() {
		return this.handler;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return this.key;
	}

}
