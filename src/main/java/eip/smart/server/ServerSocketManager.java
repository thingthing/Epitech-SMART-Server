package eip.smart.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eip.smart.server.net.tcp.TCPHandler;
import eip.smart.server.net.tcp.TCPPacketCodecFactory;
import eip.smart.server.net.udp.UDPHandler;
import eip.smart.server.net.udp.UDPPacketCodecFactory;

public class ServerSocketManager {

	private final static Logger	LOGGER		= LoggerFactory.getLogger(ServerSocketManager.class);

	private NioSocketAcceptor	acceptorTCP	= new NioSocketAcceptor();
	private NioDatagramAcceptor	acceptorUDP	= new NioDatagramAcceptor();

	public void init() {
		// Config of TCP Acceptor
		this.acceptorTCP.setCloseOnDeactivation(true);
		this.acceptorTCP.getSessionConfig().setReuseAddress(true);
		this.acceptorTCP.getFilterChain().addLast("logger", new LoggingFilter());
		this.acceptorTCP.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new TCPPacketCodecFactory()));
		this.acceptorTCP.setHandler(new TCPHandler().setIoAgentContainer(Server.getServer().getIoAgentContainer()));
		// this.acceptorTCP.getSessionConfig().setReadBufferSize(2048);
		this.acceptorTCP.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 0);
		try {
			this.socketTCPListen();
		} catch (IllegalArgumentException | IOException e) {
			ServerSocketManager.LOGGER.error("Unable to open TCP socket", e);
		}

		// Config of UDP Acceptor
		this.acceptorUDP.setCloseOnDeactivation(true);
		this.acceptorUDP.getSessionConfig().setReuseAddress(true);
		this.acceptorUDP.getFilterChain().addLast("logger", new LoggingFilter());
		this.acceptorUDP.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new UDPPacketCodecFactory()));
		this.acceptorUDP.setHandler(new UDPHandler());
		// this.acceptorUDP.getSessionConfig().setReadBufferSize(2048);
		// this.acceptorUDP.getSessionConfig().setReadBufferSize(65536);
		this.acceptorUDP.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 0);
		try {
			this.socketUDPListen();
		} catch (IOException e) {
			ServerSocketManager.LOGGER.error("Unable to open UDP socket", e);
		}
	}

	/**
	 * Open the TCP acceptor so it will handle new TCP connections.
	 *
	 * @throws IOException
	 * @param server
	 *            TODO
	 * @throws IllegalArgumentException
	 */
	public void socketTCPListen() throws IOException, IllegalArgumentException {
		this.acceptorTCP.bind(new InetSocketAddress(Server.getServer().getConf().getPropertyInteger("TCP_PORT")));
		Server.LOGGER.info("TCP Server open on port " + Server.getServer().getConf().getPropertyInteger("TCP_PORT"));
	}

	/**
	 * Stop the TCP acceptor so it will not longer handle TCP connections.
	 *
	 * @param server
	 *            TODO
	 */
	public void socketTCPListenStop() {
		for (IoSession session : this.acceptorTCP.getManagedSessions().values())
			session.close(true);
		this.acceptorTCP.unbind();
	}

	/**
	 * Open the UDP acceptor so it will handle new UDP connections.
	 *
	 * @throws IOException
	 * @param server
	 *            TODO
	 * @throws IllegalArgumentException
	 */
	public void socketUDPListen() throws IOException, IllegalArgumentException {
		this.acceptorUDP.bind(new InetSocketAddress(Server.getServer().getConf().getPropertyInteger("UDP_PORT")));
		Server.LOGGER.info("UDP Server open on port " + Server.getServer().getConf().getPropertyInteger("UDP_PORT"));
	}

	/**
	 * Stop the UDP acceptor so it will not longer handle UDP connections.
	 *
	 * @param server
	 *            TODO
	 */
	public void socketUDPListenStop() {
		for (IoSession session : this.acceptorUDP.getManagedSessions().values())
			session.close(true);
		this.acceptorUDP.unbind();
	}
}