package eip.smart.server.net;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * Implementation of IoHandler to handle Agents.
 *
 * @author Pierre Demessence
 *
 */
public class AgentServerHandler implements IoHandler {

	private IoAgentContainer	ioAgentContainer	= null;

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		this.removeSession(session);
	}

	/**
	 * Handle a new TCP message. Will allow to exit, connect and disconnect from an Agent.
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		Packet packet = (Packet) message;
		String msg = new String(packet.getData());
		if (msg.equals("exit"))
			session.close(true);
		else if (this.ioAgentContainer.getBySession(session).getAgent() == null) {
			if (msg.startsWith("name:")) {
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
			} else
				session.write("error: not authenticated");
		} else if (msg.equals("delete"))
			this.ioAgentContainer.getBySession(session).removeAgent();
		else
			this.ioAgentContainer.getBySession(session).getAgent().receiveMessage(msg);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * Delete the session and remote it from the bound IoAgent.
	 *
	 * @param session
	 */
	private void removeSession(IoSession session) {
		IoAgent ioAgent = this.ioAgentContainer.getBySession(session);
		if (ioAgent != null)
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

	/**
	 * Set the IoAgentContainer to store the connected agents.
	 *
	 * @param ioAgentContainer
	 */
	public void setIoAgentContainer(IoAgentContainer ioAgentContainer) {
		this.ioAgentContainer = ioAgentContainer;
	}

}
