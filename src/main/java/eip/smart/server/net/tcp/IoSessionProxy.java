package eip.smart.server.net.tcp;

import java.net.SocketAddress;
import java.util.Date;

import org.apache.mina.core.session.IoSession;

import eip.smart.model.proxy.Proxy;

/**
 * Proxy class for sending data in JSON.
 *
 * @author Pierre Demessence
 *
 */
public class IoSessionProxy extends Proxy<IoSession> {

	/**
	 * The name of the connected Agent if any.
	 */
	String			connectedAgent;

	/**
	 * Date of connection if the TCP session.
	 */
	Date			creationTime;

	/**
	 * The local IP of the TCP session.
	 */
	SocketAddress	localAddress;

	/**
	 * The remote IP of the TCP session.
	 */
	SocketAddress	remoteAddress;

	public IoSessionProxy() {}

	/**
	 * Create a proxy from a IoSession object.
	 * 
	 * @param object
	 */
	public IoSessionProxy(IoSession object) {
		super(object);
		this.setRemoteAddress(object.getRemoteAddress());
		this.setLocalAddress(object.getLocalAddress());
		this.setCreationTime(object.getCreationTime());
	}

	public String getConnectedAgent() {
		return (this.connectedAgent);
	}

	public String getCreationTime() {
		return (this.creationTime.toString());
	}

	public SocketAddress getLocalAddress() {
		return (this.localAddress);
	}

	public SocketAddress getRemoteAddress() {
		return (this.remoteAddress);
	}

	public void setConnectedAgent(String connectedAgent) {
		this.connectedAgent = connectedAgent;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = new Date(creationTime);
	}

	public void setLocalAddress(SocketAddress localAddress) {
		this.localAddress = localAddress;
	}

	public void setRemoteAddress(SocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
}