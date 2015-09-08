package eip.smart.server.net.udp;

public abstract class UDPPacket {

	public enum Type {
		LANDMARK,
		POINTCLOUD;
	}

	public static final short	MAX_PACKET_SIZE	= 512;

	protected long				chunkID;

	public UDPPacket(long chunkID) {
		this.chunkID = chunkID;
	}

	public abstract Type getType();

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UDPPacket [chunkID=" + this.chunkID + "]";
	}

}