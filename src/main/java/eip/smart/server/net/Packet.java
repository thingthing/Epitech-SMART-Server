package eip.smart.server.net;

import com.fasterxml.jackson.databind.JsonNode;

public class Packet {
	public static final byte	MAGIC				= 0x42;
	public static final byte	PROTOCOL_VERSION	= 1;
	public static final byte	HEADER_SIZE			= 5;

	public static final short	MAX_PACKET_SIZE		= Short.SIZE - Packet.HEADER_SIZE;

	private short				packetSize			= 5;
	private byte				protocolVersion		= Packet.PROTOCOL_VERSION;
	private byte				headerSize			= Packet.HEADER_SIZE;
	private byte[]				payload;
	private JsonNode			jsonPayload;

	/**
	 * When encoding a packet.
	 */
	public Packet(byte[] payload) {
		this.payload = payload;
		this.packetSize = (short) (this.headerSize + this.payload.length);
	}

	/**
	 * When decoding a packet
	 */
	public Packet(short packetSize, byte protocolVersion, byte headerSize, byte[] payload, JsonNode jsonPayload) {
		this.packetSize = packetSize;
		this.protocolVersion = protocolVersion;
		this.headerSize = headerSize;
		this.payload = payload;
		this.jsonPayload = jsonPayload;
	}

	/**
	 * @return the headerSize
	 */
	public byte getHeaderSize() {
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
	public short getPacketSize() {
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
	public byte getProtocolVersion() {
		return this.protocolVersion;
	}

	public int getStatusCode() {
		return (this.jsonPayload.get("status").get("code").asInt());
	}

	public String getStatusMessage() {
		return (this.jsonPayload.get("status").get("message").asText());
	}
}
