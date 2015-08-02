package eip.smart.server.net.udp;

public abstract class UDPPacket {

	public enum Type {
		LANDMARK,
		POINTCLOUD;
	}

	public class UDPPacketPoint {
		public float	x;
		public float	y;
		public float	z;
	}

	public static final short	MAX_PACKET_SIZE	= 512;

	protected long				chunkID;

	public UDPPacket(long chunkID) {
		this.chunkID = chunkID;
	}

	public abstract Type getType();

}