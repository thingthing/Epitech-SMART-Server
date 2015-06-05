package eip.smart.server.net;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class PacketCodecFactory implements ProtocolCodecFactory {

	private ProtocolEncoder	encoder;
	private ProtocolDecoder	decoder;

	public PacketCodecFactory() {
		this.encoder = new PacketEncoder();
		this.decoder = new PacketDecoder();
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return (this.decoder);
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return (this.encoder);
	}

}
