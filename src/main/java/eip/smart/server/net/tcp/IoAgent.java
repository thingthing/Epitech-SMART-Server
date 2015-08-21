package eip.smart.server.net.tcp;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.Date;

import org.apache.mina.core.session.IoSession;

import com.fasterxml.jackson.annotation.JsonView;

import eip.smart.cscommons.model.JSONViews;
import eip.smart.server.model.agent.AgentLogic;
import eip.smart.server.model.agent.AgentLogic.sendMessageCallback;

/**
 * Binding class between an Agent and a TCP IoSession.
 *
 * @author Pierre Demessence
 *
 */
public class IoAgent implements Serializable {

	@JsonView(JSONViews.IMPORTANT.class)
	private AgentLogic	agent	= null;

	private IoSession	session	= null;

	/**
	 * Construct from an Agent.
	 *
	 * @param agent
	 */
	public IoAgent(AgentLogic agent) {
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
		this.agent = new AgentLogic(name);
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
	public AgentLogic getAgent() {
		return (this.agent);
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getCreationTime()
	 */
	@JsonView(JSONViews.IMPORTANT.class)
	private Date getCreationTime() {
		return new Date(this.session.getCreationTime());
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getLocalAddress()
	 */
	@JsonView(JSONViews.IMPORTANT.class)
	private SocketAddress getLocalAddress() {
		return this.session.getLocalAddress();
	}

	/**
	 * @return
	 * @see org.apache.mina.core.session.IoSession#getRemoteAddress()
	 */
	@JsonView(JSONViews.IMPORTANT.class)
	private SocketAddress getRemoteAddress() {
		return this.session.getRemoteAddress();
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
