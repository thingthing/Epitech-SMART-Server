package eip.smart.server.net.tcp;

import java.util.ArrayList;

import org.apache.mina.core.session.IoSession;

import eip.smart.server.model.agent.AgentLogic;

/**
 * ArrayList container for IoAgents.
 *
 * @author Pierre Demessence
 *
 */
public class IoAgentContainer extends ArrayList<IoAgent> {

	/**
	 * Add an IoAgent from an agent to the list;
	 *
	 * @param agent
	 *            the agent to add.
	 */
	public void addAgent(AgentLogic agent) {
		this.add(new IoAgent(agent));
	}

	/**
	 * Add an IoAgent from a TCP session to the list.
	 *
	 * @param session
	 *            the TCP session to add.
	 */
	public void addSession(IoSession session) {
		this.add(new IoAgent(session));
	}

	/**
	 * Get the list of all the agents contained in each IoAgent.
	 *
	 * @return a list of all the agents.
	 */
	public ArrayList<AgentLogic> getAgents() {
		ArrayList<AgentLogic> agents = new ArrayList<>();
		for (IoAgent ioAgent : this)
			if (ioAgent.getAgent() != null)
				agents.add(ioAgent.getAgent());
		return (agents);
	}

	/**
	 * Return an IoAgent from an agent.
	 *
	 * @param agent
	 *            the agent of the IoAgent to get.
	 * @return The corresponding IoAgent.
	 */
	public IoAgent getByAgent(AgentLogic agent) {
		for (IoAgent ioAgent : this)
			if (ioAgent.getAgent() == agent)
				return (ioAgent);
		return (null);
	}

	/**
	 * Return an IoAgent from an agent name
	 *
	 * @param name
	 *            the name of the agent of the IoAgent to get.
	 * @return The corresponding IoAgent
	 */
	public IoAgent getByAgentName(String name) {
		for (IoAgent ioAgent : this)
			if (ioAgent.getAgent() != null && ioAgent.getAgent().getName().equals(name))
				return (ioAgent);
		return (null);
	}

	/**
	 * Return an IoAgent from a TCP session.
	 *
	 * @param session
	 *            the TCP session of the IoAgent to get.
	 * @return
	 */
	public IoAgent getBySession(IoSession session) {
		for (IoAgent ioAgent : this)
			if (ioAgent.getSession() == session)
				return (ioAgent);
		return (null);
	}

	/**
	 * Get the list of all the TCP sessions contained in each IoAgent.
	 *
	 * @return a list of all the TCP sessions.
	 */
	public ArrayList<IoSession> getSessions() {
		ArrayList<IoSession> sessions = new ArrayList<>();
		for (IoAgent ioAgent : this)
			if (ioAgent.getSession() != null)
				sessions.add(ioAgent.getSession());
		return (sessions);
	}

}
