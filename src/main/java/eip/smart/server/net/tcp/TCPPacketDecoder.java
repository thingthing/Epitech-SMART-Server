package eip.smart.server.net.tcp;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TCPPacketDecoder extends ProtocolDecoderAdapter {

	private final static Logger	LOGGER	= LoggerFactory.getLogger(TCPPacketDecoder.class);

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		try {
			TCPPacketDecoder.LOGGER.debug("Endianness : {}", ByteOrder.nativeOrder());
			TCPPacketDecoder.LOGGER.debug("Received TCP packet of size {} from {}", in.remaining(), session.getRemoteAddress());
			TCPPacketDecoder.LOGGER.debug("HexDump : {}", in.getHexDump());
			if (in.remaining() >= TCPPacket.HEADER_SIZE) {
				// Magic
				short magic = in.getUnsigned();
				if (magic != TCPPacket.MAGIC) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded : Wrong magic");
					return;
				}

				// Packet Size
				int packetSize = in.getUnsignedShort();
				TCPPacketDecoder.LOGGER.debug("TCP packet packetSize : {}", packetSize);
				if (packetSize != in.limit()) {
					TCPPacketDecoder.LOGGER.warn("Warning : TCP packet buffer size of {} does not match received packet size of {}", in.limit(), packetSize);
					// return;
				}
				if (packetSize > TCPPacket.MAX_PACKET_SIZE) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded : packet size of {} is bigger than max packet size of {}", packetSize, TCPPacket.MAX_PACKET_SIZE);
					return;
				}

				// Protocol Version
				int protocolVersion = in.getUnsignedShort();
				TCPPacketDecoder.LOGGER.debug("TCP packet protocolVersion : {}", protocolVersion);
				if (protocolVersion > TCPPacket.PROTOCOL_VERSION)
					TCPPacketDecoder.LOGGER.warn("Warning : Protocol Version mismatch : TCP packet received uses version {}, Server uses version {}", protocolVersion, TCPPacket.PROTOCOL_VERSION);

				// Header Size
				int headerSize = in.getUnsignedShort();
				TCPPacketDecoder.LOGGER.debug("TCP packet headersize : {}", headerSize);
				if (headerSize < TCPPacket.HEADER_SIZE) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded : required minimal header size of {}, {} given", TCPPacket.HEADER_SIZE, headerSize);
					return;
				}
				if (headerSize > TCPPacket.HEADER_SIZE) {
					in.skip(headerSize - TCPPacket.HEADER_SIZE);
					TCPPacketDecoder.LOGGER.warn("Warning : Skipped {} bytes as given header size of {} is bigger than required header size of {}", headerSize - TCPPacket.HEADER_SIZE, headerSize, TCPPacket.HEADER_SIZE);
				}

				// Payload
				byte[] payload = new byte[packetSize - headerSize];
				if (payload.length == 0) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded : no data");
					return;
				}
				if (in.remaining() != payload.length) {
					TCPPacketDecoder.LOGGER.warn("Warning : TCP packet expected data size of {}, {} given", payload.length, in.remaining());
					// return;
				}
				in.get(payload);

				// JSON Deserialization
				JsonNode jsonPayload = null;
				try {
					jsonPayload = new ObjectMapper().readTree(payload);
				} catch (JsonMappingException e) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded :", e);
					return;
				}

				// Good packet
				TCPPacket packet = new TCPPacket(packetSize, protocolVersion, headerSize, payload, jsonPayload);
				out.write(packet);
			} else
				TCPPacketDecoder.LOGGER.warn("TCP packet discarded : size too low. Minimal size of {} for header", TCPPacket.HEADER_SIZE);
		} catch (Exception e) {
			throw e;
		} finally {
			TCPPacketDecoder.LOGGER.warn("Warning : Skipped {} unused bytes at the end of the buffer", in.remaining());
			in.skip(in.remaining());
		}
	}
}
