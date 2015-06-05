package eip.smart.server.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class ProtoTest extends IoHandlerAdapter {

	public static void main(String[] args) {
		new ProtoTest();
	}

	private SocketConnector	connector;

	public ProtoTest() {
		this.connector = new NioSocketConnector();
		this.connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PacketCodecFactory()));
		this.connector.setHandler(this);

		IoSession session;

		ConnectFuture future = this.connector.connect(new InetSocketAddress("127.0.0.1", 4200));
		future.awaitUninterruptibly();
		session = future.getSession();

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String msg;
		while (true)
			try {
				msg = bufferedReader.readLine();
				if (msg.equals("exit"))
					return;
				session.write(msg);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		Packet packet = (Packet) message;
		System.out.println(new String(packet.getData()));

	}
}
