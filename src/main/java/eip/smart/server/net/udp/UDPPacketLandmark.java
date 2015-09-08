package eip.smart.server.net.udp;

import eip.smart.cscommons.model.geometry.Point3D;

public class UDPPacketLandmark extends UDPPacket {
	public static final byte	HEADER_SIZE	= 53;								// 8 + 1 + 12 + 12 + 4 + 4 + 4 + 4 + 4;
	public static final byte	MAGIC		= 'L';
	public static final byte	PACKET_SIZE	= UDPPacketLandmark.HEADER_SIZE;

	private float				bearing;
	private int					ID;
	private int					life;
	private Point3D				pos;
	private float				range;
	private Point3D				robotPos;
	private int					totalTimeObserved;

	/**
	 * When decoding a Packet
	 */
	public UDPPacketLandmark(long chunkID, Point3D pos, Point3D robotPos, int ID, int life, int totalTimeObserved, float bearing, float range) {
		super(chunkID);
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
	public Point3D getPos() {
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
	public Point3D getRobotPos() {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UDPPacketLandmark [pos=" + this.pos + ", robotPos=" + this.robotPos + ", ID=" + this.ID + ", life=" + this.life + ", totalTimeObserved=" + this.totalTimeObserved + ", bearing=" + this.bearing + ", range=" + this.range + ", getType()=" + this.getType() + ", toString()=" + super.toString() + "]";
	}

}
