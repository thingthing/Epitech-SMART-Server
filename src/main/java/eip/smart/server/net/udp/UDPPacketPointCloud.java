package eip.smart.server.net.udp;

public class UDPPacketPointCloud extends UDPPacket {

	public static final byte	MAGIC		= 0x44;
	public static final byte	HEADER_SIZE	= 53;	// 8 + 1 + 12 + 12 + 4 + 4 + 4 + 4 + 4;

	private int					packetID;
	private int					currentPart;
	private int					TotalPart;
	private short				packetSize;
	private UDPPacketPoint[]	data;

	/**
	 * @return the currentPart
	 */
	public int getCurrentPart() {
		return this.currentPart;
	}

	/**
	 * @return the data
	 */
	public UDPPacketPoint[] getData() {
		return this.data;
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
		return this.TotalPart;
	}

	@Override
	public Type getType() {
		return Type.POINTCLOUD;
	}

}
