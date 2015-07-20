package eip.smart.server.net;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PacketDecoder extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		if (in.remaining() >= 5) {
			byte magic = in.get();
			if (magic != Packet.MAGIC)
				return (false);
			short packetSize = in.getShort();
			byte protocolVersion = in.get();
			byte headerSize = in.get();
			if (headerSize > Packet.HEADER_SIZE)
				in.skip(headerSize - Packet.HEADER_SIZE);
			byte[] payload = new byte[packetSize - headerSize];
			in.get(payload);
			JsonNode jsonPayload = new ObjectMapper().readTree(payload);
			Packet packet = new Packet(packetSize, protocolVersion, headerSize, payload, jsonPayload);
			out.write(packet);
			return (true);
		}
		return (false);
	}
}
