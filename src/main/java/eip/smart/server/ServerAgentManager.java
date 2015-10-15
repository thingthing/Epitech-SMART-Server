package eip.smart.server;

import java.util.List;

import org.apache.mina.core.session.IoSession;

import eip.smart.server.model.agent.AgentLogic;
import eip.smart.server.net.tcp.IoAgentContainer;

public class ServerAgentManager {
	/**
	 * The ioAgentContainer to store Agents and bound TCP sessions.
	 */
	private IoAgentContainer	ioAgentContainer	= new IoAgentContainer();

	public AgentLogic getAgentByName(String name) {
		if (name != null)
			for (AgentLogic a : this.getAgentsAvailable())
				if (name.equals(a.getName()))
					return (a);
		return null;
	}

	/**
	 * Get all the connected agents.
	 *
	 * @return
	 */
	public List<AgentLogic> getAgentsAvailable() {
		return (this.ioAgentContainer.getAgents());
	}

	/**
	 * Get the IoAgentContainer which store the Agents and TCP sessions.
	 *
	 * @return
	 */
	public IoAgentContainer getIoAgentContainer() {
		return (this.ioAgentContainer);
	}

	/**
	 * Get all the connected TCP sessions.
	 *
	 * @return
	 */
	public List<IoSession> getSessions() {
		return (this.ioAgentContainer.getSessions());
	}
}
