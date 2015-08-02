package eip.smart.server.net.udp;

public abstract class UDPPacket {

	public enum Type {
		LANDMARK,
		POINTCLOUD;
	}

	public static class UDPPacketPoint {
		private float	x;
		private float	y;
		private float	z;

		public UDPPacketPoint(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public float getX() {
			return this.x;
		}

		public float getY() {
			return this.y;
		}

		public float getZ() {
			return this.z;
		}

	}

	public static final short	MAX_PACKET_SIZE	= 512;

	protected long				chunkID;

	public UDPPacket(long chunkID) {
		this.chunkID = chunkID;
	}

	public abstract Type getType();

}