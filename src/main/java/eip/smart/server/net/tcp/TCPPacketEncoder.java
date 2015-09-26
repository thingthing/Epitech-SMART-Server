package eip.smart.server.net.tcp;

import java.io.StringWriter;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.cscommons.model.JSONViews;
import eip.smart.server.model.agent.TCPMessagePacket;

public class TCPPacketEncoder implements ProtocolEncoder {

	private final static Logger	LOGGER	= LoggerFactory.getLogger(TCPPacketEncoder.class);

	private ObjectMapper		mapper	= new ObjectMapper();

	public TCPPacketEncoder() {
		this.mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
	}

	@Override
	public void dispose(IoSession session) throws Exception {}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		TCPMessagePacket messagePacket = (TCPMessagePacket) message;
		StringWriter writer = new StringWriter();
		JsonGenerator json = new JsonFactory().createGenerator(writer);

		json.writeStartObject();

		json.writeFieldName("data");
		json.writeStartObject();
		for (ImmutablePair<String, Object> p : messagePacket.getData()) {
			json.writeFieldName(p.getKey());
			this.mapper.writerWithView(JSONViews.TCP.class).writeValue(json, p.getValue());
		}
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
		TCPPacketEncoder.LOGGER.debug("Send {} to {}", packet, session.getRemoteAddress());
	}
}
