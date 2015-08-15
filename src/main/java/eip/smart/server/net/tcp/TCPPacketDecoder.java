package eip.smart.server.net.tcp;

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
			TCPPacketDecoder.LOGGER.debug("Received TCP packet of size {} from {}", in.remaining(), session.getRemoteAddress());
			if (in.remaining() >= TCPPacket.HEADER_SIZE) {
				byte magic = in.get();
				if (magic != TCPPacket.MAGIC) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded : Wrong magic");
					return;
				}
				short packetSize = in.getShort();
				byte protocolVersion = in.get();
				byte headerSize = in.get();
				if (headerSize > TCPPacket.HEADER_SIZE)
					in.skip(headerSize - TCPPacket.HEADER_SIZE);
				byte[] payload = new byte[packetSize - headerSize];
				if (in.remaining() != payload.length) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded : expected data size of {}, given data size of {}", payload.length, in.remaining());
					return;
				}
				in.get(payload);
				JsonNode jsonPayload = null;
				try {
					jsonPayload = new ObjectMapper().readTree(payload);
				} catch (JsonMappingException e) {
					TCPPacketDecoder.LOGGER.warn("TCP packet discarded :", e);
					return;
				}
				TCPPacket packet = new TCPPacket(packetSize, protocolVersion, headerSize, payload, jsonPayload);
				out.write(packet);
			} else
				TCPPacketDecoder.LOGGER.warn("TCP packet discarded : header size too low");
		} catch (Exception e) {
			throw e;
		} finally {
			in.skip(in.remaining());
		}
	}
}
