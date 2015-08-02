package eip.smart.server.net.udp;

public class UDPPacketLandmark extends UDPPacket {
	public static final byte	MAGIC		= 0x43;
	public static final byte	HEADER_SIZE	= 53;								// 8 + 1 + 12 + 12 + 4 + 4 + 4 + 4 + 4;
	public static final byte	PACKET_SIZE	= UDPPacketLandmark.HEADER_SIZE;

	private UDPPacketPoint		pos;
	private UDPPacketPoint		robotPos;
	private int					ID;
	private int					life;
	private int					totalTimeObserved;
	private float				bearing;
	private float				range;

	/**
	 * When decoding a Packet
	 */
	public UDPPacketLandmark(UDPPacketPoint pos, UDPPacketPoint robotPos, int ID, int life, int totalTimeObserved, float bearing, float range) {
		this.pos = pos;
		this.robotPos = robotPos;
		this.ID = ID;
		this.life = life;
		this.totalTimeObserved = totalTimeObserved;
		this.bearing = bearing;
		this.range = range;
	}

	/**
	 * @return the bearing
	 */
	public float getBearing() {
		return this.bearing;
	}

	/**
	 * @return the iD
	 */
	public int getID() {
		return this.ID;
	}

	/**
	 * @return the life
	 */
	public int getLife() {
		return this.life;
	}

	/**
	 * @return the pos
	 */
	public UDPPacketPoint getPos() {
		return this.pos;
	}

	/**
	 * @return the range
	 */
	public float getRange() {
		return this.range;
	}

	/**
	 * @return the robotPos
	 */
	public UDPPacketPoint getRobotPos() {
		return this.robotPos;
	}

	/**
	 * @return the totalTimeObserved
	 */
	public int getTotalTimeObserved() {
		return this.totalTimeObserved;
	}

	@Override
	public Type getType() {
		return Type.LANDMARK;
	}

}
