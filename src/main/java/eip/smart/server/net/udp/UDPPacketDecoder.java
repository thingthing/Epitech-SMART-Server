package eip.smart.server.net.udp;

import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eip.smart.cscommons.model.geometry.Point3D;
import eip.smart.server.Server;

@SuppressWarnings({ "static-method", "unused" })
public class UDPPacketDecoder extends ProtocolDecoderAdapter {

	private final static Logger	LOGGER	= LoggerFactory.getLogger(UDPPacketDecoder.class);

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		try {
			int bufferSize = in.remaining();
			int oldPos = in.position();
			UDPPacketDecoder.LOGGER.debug("Received UDP packet of size {} from {}", bufferSize, session.getRemoteAddress());
			UDPPacketDecoder.LOGGER.debug("HexDump : {}", in.getHexDump());

			IoSession tcpSession = null;
			for (IoSession s : Server.getServer().getSessions())
				if (((InetSocketAddress) s.getRemoteAddress()).getAddress().getHostAddress().equals(((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress())) {
					tcpSession = s;
					break;
				}
			if (tcpSession == null) {
				UDPPacketDecoder.LOGGER.warn("UDP Packet discarded : No TCP session found for {}", session.getRemoteAddress());
				return;
			}
			if (Server.getServer().getIoAgentContainer().getBySession(tcpSession).getAgent() == null) {
				UDPPacketDecoder.LOGGER.warn("UDP Packet discarded : No agent found found for {}", session.getRemoteAddress());
				return;
			}

			if (in.remaining() >= 9) {
				byte magic = in.get();
				UDPPacketDecoder.LOGGER.debug("Magic : {}", magic);
				long chunkID = in.getUnsignedInt();
				UDPPacketDecoder.LOGGER.debug("Chunk ID : {}", chunkID);
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
			Point3D pos = new Point3D(in.getFloat(), in.getFloat(), in.getFloat());
			Point3D robotPos = new Point3D(in.getFloat(), in.getFloat(), in.getFloat());
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
			long packetID = in.getUnsignedInt();
			UDPPacketDecoder.LOGGER.debug("packetID : {}", packetID);
			long currentPart = in.getUnsignedInt();
			UDPPacketDecoder.LOGGER.debug("currentPart : {}", currentPart);
			long totalPart = in.getUnsignedInt();
			UDPPacketDecoder.LOGGER.debug("totalPart : {}", totalPart);
			int dataSize = in.getUnsignedShort();
			UDPPacketDecoder.LOGGER.debug("dataSize : {}", dataSize);
			// UDPPacketDecoder.LOGGER.debug("remaining : {}", in.remaining());
			float[] data = new float[dataSize / 4];
			/*
			if (in.remaining() != data.length) {
				UDPPacketDecoder.LOGGER.warn("UDP PointCloud packet discarded : expected data size of {}, given data size of {}", data.length, in.remaining());
				return;
			}
			*/
			in.asFloatBuffer().get(data);
			Point3D[] dataPoints = new Point3D[data.length / 3];
			for (int i = 0; i < data.length; i += 3)
				dataPoints[i / 3] = new Point3D(data[i], data[i + 1], data[i + 2]);
			UDPPacketPointCloud packet = new UDPPacketPointCloud(chunkID, packetID, currentPart, totalPart, dataSize, data, dataPoints);
			out.write(packet);
		} else
			UDPPacketDecoder.LOGGER.debug("UDP PointCloud packet discarded : header size too low");
	}
}
