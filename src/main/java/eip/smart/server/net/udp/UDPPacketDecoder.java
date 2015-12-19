package eip.smart.server.net.udp;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eip.smart.cscommons.model.geometry.Point3D;

@SuppressWarnings({ "static-method", "unused" })
public class UDPPacketDecoder extends CumulativeProtocolDecoder {

	private final static Logger LOGGER = LoggerFactory.getLogger(UDPPacketDecoder.class);

	private boolean decodeLandmark(IoSession session, IoBuffer in, ProtocolDecoderOutput out, long chunkID, int oldPos) throws Exception {
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
			return (true);
		}
		UDPPacketDecoder.LOGGER.debug("UDP Landmark packet discarded : header size too low");
		in.position(oldPos);
		return (false);
	}

	private boolean decodePointCloud(IoSession session, IoBuffer in, ProtocolDecoderOutput out, long chunkID, int oldPos) throws Exception {
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

			if (dataSize > in.remaining()) {
				UDPPacketDecoder.LOGGER.warn("Warning : remaining buffer size of {} is too little to contain received data size of {}", in.remaining(), dataSize);
				in.position(oldPos);
				return (false);
			}
			float[] data = new float[dataSize / 4];
			in.asFloatBuffer().get(data);
			in.position(in.position() + dataSize);
			Point3D[] dataPoints = new Point3D[data.length / 6];
			for (int i = 0; i < data.length; i += 6)
				dataPoints[i / 6] = new Point3D(data[i], data[i + 1], data[i + 2], data[i + 3], data[i + 4], data[i + 5], 0);
			UDPPacketPointCloud packet = new UDPPacketPointCloud(chunkID, packetID, currentPart, totalPart, dataSize, data, dataPoints);
			out.write(packet);
			UDPPacketDecoder.LOGGER.debug("UDP PointCloud packet good.");
			return (true);
		}
		UDPPacketDecoder.LOGGER.debug("UDP PointCloud packet discarded : header size too low");
		in.position(oldPos);
		return (false);
	}

	@Override
	public boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		try {
			int bufferSize = in.remaining();
			int oldPos = in.position();
			UDPPacketDecoder.LOGGER.debug("Received UDP packet of size {} from {}", bufferSize, session.getRemoteAddress());
			UDPPacketDecoder.LOGGER.debug("HexDump : {}", in.getHexDump());

			/*
			IoSession tcpSession = null;
			for (IoSession s : Server.getServer().getSessions())
				if (((InetSocketAddress) s.getRemoteAddress()).getAddress().getHostAddress().equals(((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress())) {
					tcpSession = s;
					break;
				}

			if (tcpSession == null) {
				UDPPacketDecoder.LOGGER.warn("UDP Packet discarded : No TCP session found for {}", session.getRemoteAddress());
				return (false);
			}
			if (Server.getServer().getIoAgentContainer().getBySession(tcpSession).getAgent() == null) {
				UDPPacketDecoder.LOGGER.warn("UDP Packet discarded : No agent found found for {}", session.getRemoteAddress());
				return (false);
			}*/
			boolean res;
			if (in.remaining() >= 9) {
				byte magic = in.get();
				UDPPacketDecoder.LOGGER.debug("Magic : {}", magic);
				long chunkID = in.getUnsignedInt();
				UDPPacketDecoder.LOGGER.debug("Chunk ID : {}", chunkID);
				if (magic == UDPPacketLandmark.MAGIC)
					res = this.decodeLandmark(session, in, out, chunkID, oldPos);
				else if (magic == UDPPacketPointCloud.MAGIC)
					res = this.decodePointCloud(session, in, out, chunkID, oldPos);
				else {
					UDPPacketDecoder.LOGGER.warn("UDP packet discarded : wrong magic");
					res = false;
				}
			} else {
				UDPPacketDecoder.LOGGER.warn("UDP packet discarded : size too low");
				res = false;
			}
			UDPPacketDecoder.LOGGER.warn("UDPPacketDecoder ended with state {}", res);
			if (!res)
				throw new Exception("Unable to decode.");
			return (res);
		} catch (Exception e) {
			throw e;
		}
	}
}
