package eip.smart.server.net;

public class Packet {
	public static final byte	MAGIC				= 0x42;
	public static final byte	PROTOCOL_VERSION	= 1;
	public static final byte	HEADER_SIZE			= 5;

	public static final short	MAX_PACKET_SIZE		= Short.SIZE - Packet.HEADER_SIZE;

	private short				packetSize			= 5;
	private byte				protocolVersion		= Packet.PROTOCOL_VERSION;
	private byte				headerSize			= Packet.HEADER_SIZE;
	private byte[]				data;

	public Packet(byte[] data) {
		this.data = data;
		this.packetSize = (short) (this.headerSize + this.data.length);
	}

	public Packet(short packetSize, byte protocolVersion, byte headerSize, byte[] data) {
		this.packetSize = packetSize;
		this.protocolVersion = protocolVersion;
		this.headerSize = headerSize;
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return this.data;
	}

	/**
	 * @return the headerSize
	 */
	public byte getHeaderSize() {
		return this.headerSize;
	}

	/**
	 * @return the packetSize
	 */
	public short getPacketSize() {
		return this.packetSize;
	}

	/**
	 * @return the protocolVersion
	 */
	public byte getProtocolVersion() {
		return this.protocolVersion;
	}
}
