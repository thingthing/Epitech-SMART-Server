package eip.smart.server.net;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import eip.smart.model.Agent;

public class AgentServerHandler implements IoHandler {

	private IoAgentContainer	ioAgentContainer	= null;

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		this.removeSession(session);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String msg = (String) message;
		if (msg.equals("exit"))
			session.close(true);
		else if (this.ioAgentContainer.getBySession(session).getAgent() == null && msg.startsWith("name:")) {
			String name = msg.replaceFirst("name:", "");
			if (!msg.matches("name:[a-zA-Z0-9]*") || name.isEmpty()) {
				System.out.println("1");
				session.write("error: invalid name");
				return;
			}
			IoAgent ioAgent = this.ioAgentContainer.getByAgentName(name);
			if (ioAgent != null) {
				if (ioAgent.getAgent().isConnected())
					session.write("error: name already used");
				else {
					this.ioAgentContainer.remove(this.ioAgentContainer.getBySession(session));
					ioAgent.sessionCreated(session);
				}
			} else
				this.ioAgentContainer.getBySession(session).createAgent(name);
		} else {
			Agent agent = this.ioAgentContainer.getBySession(session).getAgent();
			if (agent != null)
				agent.sendMessage(msg);
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
	}

	private void removeSession(IoSession session) {
		IoAgent ioAgent = this.ioAgentContainer.getBySession(session);
		if (ioAgent.getAgent() == null)
			this.ioAgentContainer.remove(ioAgent);
		else
			ioAgent.sessionDestroyed();
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		this.removeSession(session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		this.ioAgentContainer.addSession(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
	}

	public void setIoAgentContainer(IoAgentContainer ioAgentContainer) {
		this.ioAgentContainer = ioAgentContainer;
	}

}
