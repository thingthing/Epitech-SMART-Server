package eip.smart.server.net.tcp;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import eip.smart.server.model.agent.TCPMessagePacket;

/**
 * Implementation of IoHandler to handle Agents.
 *
 * @author Pierre Demessence
 *
 */
public class TCPHandler implements IoHandler {

	private final static Logger	LOGGER				= LoggerFactory.getLogger(TCPHandler.class);

	private IoAgentContainer	ioAgentContainer	= null;

	private void authenticate(IoSession session, TCPPacket packet) {
		TCPHandler.LOGGER.debug("Authentication succeeded", session.getRemoteAddress());
		session.write(new TCPMessagePacket().setStatus(0, "authenticated"));
		this.ioAgentContainer.getBySession(session).getAgent().receiveMessage(packet.getJsonData());
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		TCPHandler.LOGGER.error("TCP Exception", cause);
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
		TCPPacket packet = (TCPPacket) message;
		if (packet.getStatusCode() != 0) {
			TCPHandler.LOGGER.error(packet.getStatusMessage());
			return;
		}
		TCPHandler.LOGGER.debug("Received TCP data from {}", session.getRemoteAddress());
		JsonNode jsonData = packet.getJsonData();
		if (jsonData.has("exit")) {
			TCPHandler.LOGGER.debug("Session closed remotely by {}", session.getRemoteAddress());
			session.close(true);
		} else if (this.ioAgentContainer.getBySession(session).getAgent() == null) {
			if (jsonData.has("name")) {
				String name = jsonData.get("name").asText().trim();
				TCPHandler.LOGGER.debug("{} trying to authenticate as \"{}\"", session.getRemoteAddress(), name);
				if (name.isEmpty()) {
					TCPHandler.LOGGER.warn("Authentication failed : invalid name");
					session.write(new TCPMessagePacket().setStatus(1, "invalid name"));
					return;
				}
				IoAgent ioAgent = this.ioAgentContainer.getByAgentName(name);
				if (ioAgent != null) {
					if (ioAgent.getAgent().isConnected()) {
						TCPHandler.LOGGER.warn("Authentication failed : name already used");
						session.write(new TCPMessagePacket().setStatus(1, "name already used"));
					} else {
						this.ioAgentContainer.remove(this.ioAgentContainer.getBySession(session));
						ioAgent.sessionCreated(session);
						this.authenticate(session, packet);
					}
				} else {
					this.ioAgentContainer.getBySession(session).createAgent(name);
					this.authenticate(session, packet);
				}
			} else {
				TCPHandler.LOGGER.warn("TCP data discarded : {} not authenticated", session.getRemoteAddress());
				session.write(new TCPMessagePacket().setStatus(1, "not authenticated"));
			}
		} else
			this.ioAgentContainer.getBySession(session).getAgent().receiveMessage(packet.getJsonData());
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {}

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
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {}

	@Override
	public void sessionOpened(IoSession session) throws Exception {}

	/**
	 * Set the IoAgentContainer to store the connected agents.
	 *
	 * @param ioAgentContainer
	 */
	public void setIoAgentContainer(IoAgentContainer ioAgentContainer) {
		this.ioAgentContainer = ioAgentContainer;
	}

}
