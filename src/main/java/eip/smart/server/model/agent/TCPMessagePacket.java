package eip.smart.server.model.agent;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class TCPMessagePacket {
	private int									statusCode		= 0;
	private String								statusMessage	= "";
	private List<ImmutablePair<String, Object>>	data			= new ArrayList<>();

	public TCPMessagePacket() {}

	/**
	 * @param key
	 *            the key to add
	 * @param value
	 *            the object to add
	 */
	public TCPMessagePacket addObject(String key, Object value) {
		this.data.add(new ImmutablePair<>(key, value));
		return (this);
	}

	/**
	 * @return the data
	 */
	public List<ImmutablePair<String, Object>> getData() {
		return this.data;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return this.statusCode;
	}

	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return this.statusMessage;
	}

	/**
	 * @param statusCode
	 *            the statusCode to set
	 * @param statusMessage
	 *            the statusMessage to set
	 */
	public TCPMessagePacket setStatus(int statusCode, String statusMessage) {
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		return (this);
	}

	/**
	 * @param statusCode
	 *            the statusCode to set
	 */
	public TCPMessagePacket setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		return (this);
	}

	/**
	 * @param statusMessage
	 *            the statusMessage to set
	 */
	public TCPMessagePacket setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		return (this);
	}

}
