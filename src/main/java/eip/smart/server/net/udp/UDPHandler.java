package eip.smart.server.net.udp;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("static-method")
public class UDPHandler implements IoHandler {

	private final static Logger	LOGGER	= LoggerFactory.getLogger(UDPHandler.class);

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		UDPHandler.LOGGER.error("UDP Exception", cause);
	}

	private void handleUDPPacketLandmark(UDPPacketLandmark packet) {
		UDPHandler.LOGGER.info("Received landmark packet : {}", packet);
	}

	private void handleUDPPacketPointCloud(UDPPacketPointCloud packet) {
		UDPHandler.LOGGER.info("Received pointcloud packet : {}", packet);
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		IoSession tcpSession = null;
		for (IoSession s : Server.getServer().getSessions())
			if (((InetSocketAddress) s.getRemoteAddress()).getAddress().getHostAddress().equals(((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress())) {
				tcpSession = s;
				break;
			}
		if (tcpSession == null) {
			UDPHandler.LOGGER.warn("UDP Packet discarded : No TCP session found for {}", session.getRemoteAddress());
			return;
		}
		if (Server.getServer().getIoAgentContainer().getBySession(tcpSession).getAgent() == null) {
			UDPHandler.LOGGER.warn("UDP Packet discarded : No agent found found for {}", session.getRemoteAddress());
			return;
		}

		UDPPacket packet = (UDPPacket) message;
		if (packet.getType().equals(UDPPacket.Type.LANDMARK))
			this.handleUDPPacketLandmark((UDPPacketLandmark) packet);
		else if (packet.getType().equals(UDPPacket.Type.LANDMARK))
			this.handleUDPPacketPointCloud((UDPPacketPointCloud) packet);
		else
			UDPHandler.LOGGER.error("UDP Error : Wrong packet");
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {}

	@Override
	public void sessionClosed(IoSession session) throws Exception {}

	@Override
	public void sessionCreated(IoSession session) throws Exception {}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {}

	@Override
	public void sessionOpened(IoSession session) throws Exception {}

}
