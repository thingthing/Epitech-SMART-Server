package eip.smart.server.net;

import java.io.StringWriter;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import eip.smart.model.MessagePacket;
import eip.smart.util.Pair;

public class PacketEncoder implements ProtocolEncoder {

	@Override
	public void dispose(IoSession session) throws Exception {}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		MessagePacket messagePacket = (MessagePacket) message;
		StringWriter writer = new StringWriter();
		JsonGenerator json = new JsonFactory().createGenerator(writer);

		json.writeStartObject();

		json.writeFieldName("data");
		json.writeStartObject();
		for (Pair<String, Object> p : messagePacket.getData())
			json.writeObjectField(p.getKey(), p.getValue());
		json.writeEndObject();

		json.writeFieldName("status");
		json.writeStartObject();
		json.writeNumberField("code", messagePacket.getStatusCode());
		json.writeStringField("message", messagePacket.getStatusMessage());
		json.writeEndObject();

		json.writeEndObject();
		json.close();

		Packet packet = new Packet(writer.toString().getBytes());
		IoBuffer buffer = IoBuffer.allocate(packet.getPacketSize(), false);
		buffer.put(Packet.MAGIC);
		buffer.putShort(packet.getPacketSize());
		buffer.put(packet.getProtocolVersion());
		buffer.put(packet.getHeaderSize());
		buffer.put(packet.getPayload());
		buffer.flip();
		out.write(buffer);
	}
}
