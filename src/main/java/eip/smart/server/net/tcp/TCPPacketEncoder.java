package eip.smart.server.net.tcp;

import java.io.StringWriter;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.server.model.agent.TCPMessagePacket;

public class TCPPacketEncoder implements ProtocolEncoder {

	@Override
	public void dispose(IoSession session) throws Exception {}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		TCPMessagePacket messagePacket = (TCPMessagePacket) message;
		StringWriter writer = new StringWriter();
		JsonGenerator json = new JsonFactory().createGenerator(writer);
		json.setCodec(new ObjectMapper());

		json.writeStartObject();

		json.writeFieldName("data");
		json.writeStartObject();
		for (ImmutablePair<String, Object> p : messagePacket.getData())
			json.writeObjectField(p.getKey(), p.getValue());
		json.writeEndObject();

		json.writeFieldName("status");
		json.writeStartObject();
		json.writeNumberField("code", messagePacket.getStatusCode());
		json.writeStringField("message", messagePacket.getStatusMessage());
		json.writeEndObject();

		json.writeEndObject();
		json.close();

		TCPPacket packet = new TCPPacket(writer.toString().getBytes());
		IoBuffer buffer = IoBuffer.allocate(packet.getPacketSize(), false);
		buffer.putUnsignedShort(TCPPacket.MAGIC);
		buffer.putUnsignedShort(packet.getPacketSize());
		buffer.putUnsignedShort(packet.getProtocolVersion());
		buffer.putUnsignedShort(packet.getHeaderSize());
		buffer.put(packet.getPayload());
		buffer.flip();
		out.write(buffer);
	}
}
