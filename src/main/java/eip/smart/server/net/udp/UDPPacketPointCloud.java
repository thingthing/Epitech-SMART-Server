package eip.smart.server.net.udp;

import java.util.Arrays;

import eip.smart.cscommons.model.geometry.Point3D;

public class UDPPacketPointCloud extends UDPPacket {

	public static final byte	HEADER_SIZE	= 53;	// 8 + 1 + 12 + 12 + 4 + 4 + 4 + 4 + 4;
	public static final byte	MAGIC		= 'P';

	private long				currentPart;
	private float[]				data;
	private Point3D[]			dataPoints;
	private int					dataSize;
	private long				packetID;
	private long				totalPart;

	/**
	 * When decoding a Packet
	 */
	public UDPPacketPointCloud(long chunkID, long packetID2, long currentPart2, long totalPart2, int dataSize2, float[] data, Point3D[] dataPoints) {
		super(chunkID);
		this.packetID = packetID2;
		this.currentPart = currentPart2;
		this.totalPart = totalPart2;
		this.dataSize = dataSize2;
		this.data = data;
		this.dataPoints = dataPoints;
	}

	/**
	 * @return the currentPart
	 */
	public long getCurrentPart() {
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
	public Point3D[] getDataPoints() {
		return this.dataPoints;
	}

	/**
	 * @return the datatSize
	 */
	public int getDataSize() {
		return this.dataSize;
	}

	/**
	 * @return the packetID
	 */
	public long getPacketID() {
		return this.packetID;
	}

	/**
	 * @return the totalPart
	 */
	public long getTotalPart() {
		return this.totalPart;
	}

	@Override
	public Type getType() {
		return Type.POINTCLOUD;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UDPPacketPointCloud [packetID=" + this.packetID + ", currentPart=" + this.currentPart + ", totalPart=" + this.totalPart + ", dataSize=" + this.dataSize + ", dataPoints=" + Arrays.toString(this.dataPoints) + ", getType()=" + this.getType() + ", toString()=" + super.toString() + "]";
	}

}
