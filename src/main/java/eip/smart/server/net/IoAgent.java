package eip.smart.server.net;

import org.apache.mina.core.session.IoSession;

import eip.smart.model.Agent;
import eip.smart.model.Agent.sendMessageCallback;

/**
 * Binding class between an Agent and a TCP IoSession.
 * 
 * @author Pierre Demessence
 *
 */
public class IoAgent {
	private Agent		agent	= null;
	private IoSession	session	= null;

	/**
	 * Construct from an Agent.
	 * 
	 * @param agent
	 */
	public IoAgent(Agent agent) {
		this.agent = agent;
	}

	/**
	 * Construct from an already connected session.
	 * 
	 * @param session
	 */
	public IoAgent(IoSession session) {
		this.session = session;
	}

	/**
	 * Create a new agent for an IoAgent with only a session.
	 * 
	 * @param name
	 *            The name of the agent to create.
	 */
	public void createAgent(String name) {
		this.agent = new Agent(name);
		this.agent.setSendMessageCallback(new sendMessageCallback() {
			@Override
			public void callback(Object message) {
				if (IoAgent.this.session != null)
					IoAgent.this.session.write(message);
			}
		});
		this.agent.setConnected(true);
	}

	/**
	 * Get the agent.
	 * 
	 * @return the agent.
	 */
	public Agent getAgent() {
		return (this.agent);
	}

	/**
	 * Get the TCP IoSession
	 * 
	 * @return the TCP IoSession
	 */
	public IoSession getSession() {
		return (this.session);
	}

	/**
	 * Remote the agent bound with the IoAgent.
	 */
	public void removeAgent() {
		this.agent = null;
	}

	/**
	 * Bound a session for an IoAgent with only an Agent.
	 * 
	 * @param session
	 *            the session to bind.
	 */
	public void sessionCreated(IoSession session) {
		this.session = session;
		this.agent.setConnected(true);
	}

	/**
	 * Remove the bound session.
	 */
	public void sessionDestroyed() {
		this.session = null;
		this.agent.setConnected(false);
	}

}
