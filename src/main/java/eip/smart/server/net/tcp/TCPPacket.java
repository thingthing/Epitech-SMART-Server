package eip.smart.server.net.tcp;

import com.fasterxml.jackson.databind.JsonNode;

public class TCPPacket {
	public static final short	MAGIC				= 0x42;
	public static final int		PROTOCOL_VERSION	= 1;
	public static final int		HEADER_SIZE			= 7;

	public static final int		MAX_PACKET_SIZE		= Character.MAX_VALUE;
	public static final int		MAX_PAYLOAD_SIZE	= TCPPacket.MAX_PACKET_SIZE - TCPPacket.HEADER_SIZE;

	private int					packetSize;
	private int					protocolVersion		= TCPPacket.PROTOCOL_VERSION;
	private int					headerSize			= TCPPacket.HEADER_SIZE;
	private byte[]				payload;
	private JsonNode			jsonPayload;

	/**
	 * When encoding a packet.
	 */
	public TCPPacket(byte[] payload) {
		this.payload = payload;
		this.packetSize = this.headerSize + this.payload.length;
	}

	/**
	 * When decoding a packet
	 */
	public TCPPacket(int packetSize, int protocolVersion, int headerSize, byte[] payload, JsonNode jsonPayload) {
		this.packetSize = packetSize;
		this.protocolVersion = protocolVersion;
		this.headerSize = headerSize;
		this.payload = payload;
		this.jsonPayload = jsonPayload;
	}

	/**
	 * @return the headerSize
	 */
	public int getHeaderSize() {
		return this.headerSize;
	}

	/**
	 * @return
	 *         the json data
	 */
	public JsonNode getJsonData() {
		return (this.jsonPayload.get("data"));
	}

	/**
	 * @return the jsonPayload
	 */
	public JsonNode getJsonPayload() {
		return this.jsonPayload;
	}

	/**
	 * @return
	 *         the json status
	 */
	public JsonNode getJsonStatus() {
		return (this.jsonPayload.get("status"));
	}

	/**
	 * @return the packetSize
	 */
	public int getPacketSize() {
		return this.packetSize;
	}

	/**
	 * @return the payload
	 */
	public byte[] getPayload() {
		return this.payload;
	}

	/**
	 * @return the protocolVersion
	 */
	public int getProtocolVersion() {
		return this.protocolVersion;
	}

	public int getStatusCode() {
		if (this.getJsonStatus() == null)
			return (-1);
		return (this.getJsonStatus().get("code").asInt());
	}

	public String getStatusMessage() {
		if (this.getJsonStatus() == null)
			return (null);
		return (this.getJsonStatus().get("message").asText());
	}
}
