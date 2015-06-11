package eip.smart.server.net;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.fasterxml.jackson.databind.JsonNode;

import eip.smart.model.MessagePacket;

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
		JsonNode jsonData = packet.getJsonData();
		if (jsonData.has("exit"))
			session.close(true);
		else if (this.ioAgentContainer.getBySession(session).getAgent() == null) {
			if (jsonData.has("name")) {
				String name = jsonData.get("name").asText().trim();
				if (name.isEmpty())
					session.write(new MessagePacket().setStatus(1, "invalid name"));
				IoAgent ioAgent = this.ioAgentContainer.getByAgentName(name);
				if (ioAgent != null) {
					if (ioAgent.getAgent().isConnected())
						session.write(new MessagePacket().setStatus(1, "name already used"));
					else {
						this.ioAgentContainer.remove(this.ioAgentContainer.getBySession(session));
						ioAgent.sessionCreated(session);
						session.write(new MessagePacket().setStatus(0, "authenticated"));
						this.ioAgentContainer.getBySession(session).getAgent().receiveMessage(packet.getJsonData());

					}
				} else {
					this.ioAgentContainer.getBySession(session).createAgent(name);
					session.write(new MessagePacket().setStatus(0, "authenticated"));
					this.ioAgentContainer.getBySession(session).getAgent().receiveMessage(packet.getJsonData());
				}
			} else
				session.write(new MessagePacket().setStatus(1, "not authenticated"));
		} else
			this.ioAgentContainer.getBySession(session).getAgent().receiveMessage(packet.getJsonData());
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
