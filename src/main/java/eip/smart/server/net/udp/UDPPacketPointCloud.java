package eip.smart.server.net.udp;

import java.util.Arrays;

import eip.smart.cscommons.model.geometry.Point3D;

public class UDPPacketPointCloud extends UDPPacket {

	public static final byte	HEADER_SIZE	= 53;	// 8 + 1 + 12 + 12 + 4 + 4 + 4 + 4 + 4;
	public static final byte	MAGIC		= 'P';

	private int					currentPart;
	private float[]				data;
	private Point3D[]			dataPoints;
	private short				dataSize;
	private int					packetID;
	private int					totalPart;

	/**
	 * When decoding a Packet
	 */
	public UDPPacketPointCloud(long chunkID, int packetID, int currentPart, int totalPart, short dataSize, float[] data, Point3D[] dataPoints) {
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
	public Point3D[] getDataPoints() {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UDPPacketPointCloud [packetID=" + this.packetID + ", currentPart=" + this.currentPart + ", totalPart=" + this.totalPart + ", dataSize=" + this.dataSize + ", data=" + Arrays.toString(this.data) + ", dataPoints=" + Arrays.toString(this.dataPoints) + ", getType()=" + this.getType() + ", toString()=" + super.toString() + "]";
	}

}
