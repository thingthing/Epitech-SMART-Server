package eip.smart.server.net.udp;

public class UDPPacketPointCloud extends UDPPacket {

	public static final byte	MAGIC		= 'P';
	public static final byte	HEADER_SIZE	= 53;	// 8 + 1 + 12 + 12 + 4 + 4 + 4 + 4 + 4;

	private int					packetID;
	private int					currentPart;
	private int					totalPart;
	private short				dataSize;
	private float[]				data;
	private UDPPacketPoint[]	dataPoints;

	/**
	 * When decoding a Packet
	 */
	public UDPPacketPointCloud(long chunkID, int packetID, int currentPart, int totalPart, short dataSize, float[] data, UDPPacketPoint[] dataPoints) {
		super(chunkID);
		this.packetID = packetID;
		this.currentPart = currentPart;
		this.totalPart = totalPart;
		this.dataSize = dataSize;
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
	public float[] getData() {
		return this.data;
	}

	/**
	 * @return the dataPoints
	 */
	public UDPPacketPoint[] getDataPoints() {
		return this.dataPoints;
	}

	/**
	 * @return the datatSize
	 */
	public short getDataSize() {
		return this.dataSize;
	}

	/**
	 * @return the packetID
	 */
	public int getPacketID() {
		return this.packetID;
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
