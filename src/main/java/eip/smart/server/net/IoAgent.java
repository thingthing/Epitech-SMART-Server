package eip.smart.server.net;

import org.apache.mina.core.session.IoSession;

import eip.smart.model.Agent;
import eip.smart.model.Agent.sendMessageCallback;

public class IoAgent {
	private Agent		agent	= null;
	private IoSession	session	= null;

	public IoAgent(Agent agent) {
		this.agent = agent;
	}

	public IoAgent(IoSession session) {
		this.session = session;
	}

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

	public Agent getAgent() {
		return (this.agent);
	}

	public IoSession getSession() {
		return (this.session);
	}

	public void sessionCreated(IoSession session) {
		this.session = session;
		this.agent.setConnected(true);
	}

	public void sessionDestroyed() {
		this.session = null;
		this.agent.setConnected(false);
	}

}
