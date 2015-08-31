package eip.smart.server.net.tcp;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TCPPacketDecoder extends ProtocolDecoderAdapter {

	private final static Logger	LOGGER	= LoggerFactory.getLogger(TCPPacketDecoder.class);

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		try {
			int bufferSize = in.remaining();
			TCPPacketDecoder.LOGGER.debug("Received TCP packet of size {} from {}", bufferSize, session.getRemoteAddress());
			TCPPacketDecoder.LOGGER.debug("HexDump : {}", in.getHexDump());
			if (in.remaining() >= TCPPacket.HEADER_SIZE) {
				// Magic
				int magic = in.getUnsignedShort();
				if (magic != TCPPacket.MAGIC) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded : Wrong magic {} (expected {})", magic, TCPPacket.MAGIC);
					return;
				}

				// Packet Size
				int packetSize = in.getUnsignedShort();
				TCPPacketDecoder.LOGGER.debug("TCP packet packetSize : {}", packetSize);
				if (packetSize > bufferSize) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded : buffer size of {} is too little to contain received packet size of {}", bufferSize, packetSize);
					return;
				}
				if (packetSize < TCPPacket.HEADER_SIZE) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded : packet size of {} is too little to contain minimal header size of {}", packetSize, TCPPacket.HEADER_SIZE);
					return;
				}
				if (packetSize != bufferSize)
					TCPPacketDecoder.LOGGER.warn("Warning : TCP packet buffer size of {} does not match received packet size of {}", bufferSize, packetSize);
				if (packetSize > TCPPacket.MAX_PACKET_SIZE) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded : packet size of {} is bigger than max packet size of {}", packetSize, TCPPacket.MAX_PACKET_SIZE);
					return;
				}

				// Protocol Version
				int protocolVersion = in.getUnsignedShort();
				TCPPacketDecoder.LOGGER.debug("TCP packet protocolVersion : {}", protocolVersion);
				if (protocolVersion != TCPPacket.PROTOCOL_VERSION)
					TCPPacketDecoder.LOGGER.warn("Warning : Protocol Version mismatch : TCP packet received uses version {}, Server uses version {}", protocolVersion, TCPPacket.PROTOCOL_VERSION);

				// Header Size
				int headerSize = in.getUnsignedShort();
				TCPPacketDecoder.LOGGER.debug("TCP packet headersize : {}", headerSize);
				if (headerSize < TCPPacket.HEADER_SIZE) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded : required minimal header size of {}, {} given", TCPPacket.HEADER_SIZE, headerSize);
					return;
				}
				if (headerSize > packetSize) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded : given header size of {} is bigger than given packet size of {} ", headerSize, packetSize);
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
				if (in.remaining() != payload.length)
					TCPPacketDecoder.LOGGER.warn("Warning : TCP packet expected data size of {}, {} given", payload.length, in.remaining());
				in.get(payload);

				// JSON Deserialization
				JsonNode jsonPayload = null;
				try {
					jsonPayload = new ObjectMapper().readTree(payload);
				} catch (JsonMappingException | JsonParseException e) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded :", e);
					return;
				}
				if (jsonPayload.get("data") == null) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded : no \"data\" json object after parsing");
					return;
				}
				if (jsonPayload.get("status") == null) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded : no \"status\" json object after parsing");
					return;
				}
				if (jsonPayload.get("status").get("code") == null) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded : no \"code\" json object in \"status\" after parsing");
					return;
				}

				// Good packet
				TCPPacket packet = new TCPPacket(packetSize, protocolVersion, headerSize, payload, jsonPayload);
				out.write(packet);
			} else {
				TCPPacketDecoder.LOGGER.warn("TCP packet discarded : buffer size too low. Minimal size of {} for header", TCPPacket.HEADER_SIZE);
				in.skip(in.remaining());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (in.remaining() > 0)
				TCPPacketDecoder.LOGGER.debug("Warning : {} unused bytes at the end of the buffer", in.remaining());
		}
	}
}
