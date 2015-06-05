package eip.smart.server.net;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class PacketEncoder implements ProtocolEncoder {

	@Override
	public void dispose(IoSession session) throws Exception {}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		Packet packet = new Packet(((String) message).getBytes());
		IoBuffer buffer = IoBuffer.allocate(packet.getPacketSize(), false);
		buffer.put(Packet.MAGIC);
		buffer.putShort(packet.getPacketSize());
		buffer.put(packet.getProtocolVersion());
		buffer.put(packet.getHeaderSize());
		buffer.put(packet.getData());
		buffer.flip();
		out.write(buffer);
	}
}
