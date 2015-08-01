package eip.smart.server.net.tcp;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class TCPPacketCodecFactory implements ProtocolCodecFactory {

	private ProtocolEncoder	encoder;
	private ProtocolDecoder	decoder;

	public TCPPacketCodecFactory() {
		this.encoder = new TCPPacketEncoder();
		this.decoder = new TCPPacketDecoder();
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
