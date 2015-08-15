package eip.smart.server.net.udp;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eip.smart.server.net.udp.UDPPacket.UDPPacketPoint;

@SuppressWarnings({ "static-method", "unused" })
public class UDPPacketDecoder extends ProtocolDecoderAdapter {

	private final static Logger	LOGGER	= LoggerFactory.getLogger(UDPPacketDecoder.class);

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		try {
			UDPPacketDecoder.LOGGER.debug("Received UDP packet of size {} from {}", in.remaining(), session.getRemoteAddress());
			if (in.remaining() >= 9) {
				byte magic = in.get();
				long chunkID = in.getLong();
				if (magic == UDPPacketLandmark.MAGIC)
					this.decodeLandmark(session, in, out, chunkID);
				else if (magic == UDPPacketPointCloud.MAGIC)
					this.decodePointCloud(session, in, out, chunkID);
				else
					UDPPacketDecoder.LOGGER.warn("UDP packet discarded : wrong magic");
			} else
				UDPPacketDecoder.LOGGER.warn("UDP packet discarded : size too low");
		} catch (Exception e) {
			throw e;
		} finally {
			in.skip(in.remaining());
		}
	}

	private void decodeLandmark(IoSession session, IoBuffer in, ProtocolDecoderOutput out, long chunkID) throws Exception {
		UDPPacketDecoder.LOGGER.debug("Received UDP Landmark packet");
		if (in.remaining() >= UDPPacketLandmark.HEADER_SIZE - 9) {
			UDPPacketPoint pos = new UDPPacketPoint(in.getFloat(), in.getFloat(), in.getFloat());
			UDPPacketPoint robotPos = new UDPPacketPoint(in.getFloat(), in.getFloat(), in.getFloat());
			int ID = in.getInt();
			int life = in.getInt();
			int totalTimeObserved = in.getInt();
			float bearing = in.getFloat();
			float range = in.getFloat();
			UDPPacketLandmark packet = new UDPPacketLandmark(chunkID, pos, robotPos, ID, life, totalTimeObserved, bearing, range);
			out.write(packet);
		} else
			UDPPacketDecoder.LOGGER.debug("UDP Landmark packet discarded : header size too low");
	}

	private void decodePointCloud(IoSession session, IoBuffer in, ProtocolDecoderOutput out, long chunkID) throws Exception {
		UDPPacketDecoder.LOGGER.debug("Received UDP PointCloud packet");
		if (in.remaining() >= UDPPacketPointCloud.HEADER_SIZE - 9) {
			int packetID = in.getInt();
			int currentPart = in.getInt();
			int totalPart = in.getInt();
			short dataSize = in.getShort();
			float[] data = new float[dataSize];
			if (in.remaining() != data.length) {
				UDPPacketDecoder.LOGGER.warn("UDP PointCloud packet discarded : expected data size of {}, given data size of {}", data.length, in.remaining());
				return;
			}
			in.asFloatBuffer().get(data);
			UDPPacketPoint[] dataPoints = new UDPPacketPoint[data.length / 3];
			for (int i = 0; i < data.length; i += 3)
				dataPoints[i / 3] = new UDPPacketPoint(data[i], data[i + 1], data[i + 2]);
			UDPPacketPointCloud packet = new UDPPacketPointCloud(chunkID, packetID, currentPart, totalPart, dataSize, data, dataPoints);
			out.write(packet);
		} else
			UDPPacketDecoder.LOGGER.debug("UDP PointCloud packet discarded : header size too low");
	}
}
