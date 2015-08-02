package eip.smart.server.net.udp;

public class UDPPacketPointCloud extends UDPPacket {

	public static final byte	MAGIC		= 0x44;
	public static final byte	HEADER_SIZE	= 53;	// 8 + 1 + 12 + 12 + 4 + 4 + 4 + 4 + 4;

	private int					packetID;
	private int					currentPart;
	private int					totalPart;
	private short				packetSize;
	private byte[]				data;
	private UDPPacketPoint[]	dataPoints;

	/**
	 * When decoding a Packet
	 */
	public UDPPacketPointCloud(long chunkID, int packetID, int currentPart, int totalPart, short packetSize, byte[] data, UDPPacketPoint[] dataPoints) {
		super(chunkID);
		this.packetID = packetID;
		this.currentPart = currentPart;
		this.totalPart = totalPart;
		this.packetSize = packetSize;
		this.data = data;
		this.dataPoints = dataPoints;
	}

	/**
	 * @return the currentPart
	 */
	public int getCurrentPart() {
		return this.currentPart;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return this.data;
	}

	/**
	 * @return the dataPoints
	 */
	public UDPPacketPoint[] getDataPoints() {
		return this.dataPoints;
	}

	/**
	 * @return the packetID
	 */
	public int getPacketID() {
		return this.packetID;
	}

	/**
	 * @return the packetSize
	 */
	public short getPacketSize() {
		return this.packetSize;
	}

	/**
	 * @return the totalPart
	 */
	public int getTotalPart() {
		return this.totalPart;
	}

	@Override
	public Type getType() {
		return Type.POINTCLOUD;
	}

}
