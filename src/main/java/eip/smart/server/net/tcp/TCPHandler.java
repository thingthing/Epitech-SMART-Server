package eip.smart.server.net.tcp;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of IoHandler to handle Agents.
 *
 * @author Pierre Demessence
 *
 */
public class TCPHandler implements IoHandler {

	private final static Logger	LOGGER				= LoggerFactory.getLogger(TCPHandler.class);

	private IoAgentContainer	ioAgentContainer	= null;
	private TCPCommandManager	manager				= new TCPCommandManager();

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
		TCPHandler.LOGGER.debug("Received TCP data from {}", session.getRemoteAddress());
		TCPHandler.LOGGER.debug("{}", packet.getJsonData().toString());

		if (packet.getStatusCode() != 0) {
			TCPHandler.LOGGER.warn("Received packet with code {} and message {}", packet.getStatusCode(), packet.getStatusMessage());
			return;
		}
		this.manager.handleTCPData(packet.getJsonData(), session);
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
