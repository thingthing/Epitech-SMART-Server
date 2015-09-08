package eip.smart.server.net.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.cscommons.model.geometry.Point3D;
import eip.smart.server.model.agent.TCPMessagePacket;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TCPProtoTest extends IoHandlerAdapter {

	private static Map<String, Class>	map	= new HashMap<String, Class>() {
												{
													this.put("position", Point3D.class);
													this.put("exit", Byte.class);
													this.put("battery", Float.class);
												}
											};

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		new TCPProtoTest();
	}

	private SocketConnector	connector;

	private boolean			read	= true;

	public TCPProtoTest() {
		this.connector = new NioSocketConnector();
		this.connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TCPPacketCodecFactory()));
		this.connector.setHandler(this);

		IoSession session;

		ConnectFuture future = this.connector.connect(new InetSocketAddress("127.0.0.1", 4200)); // 54.148.17.11
		future.awaitUninterruptibly();
		session = future.getSession();

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String msg;
		while (this.read)
			try {
				// session.write(new TCPMessagePacket().addObject("name", "titi").addObject("state", 0).addObject("noz", 0));
				msg = bufferedReader.readLine();
				if (!msg.contains(":"))
					continue;
				String key = msg.substring(0, msg.indexOf(":"));
				Object value = msg.substring(msg.indexOf(":") + 1);
				for (Entry<String, Class> entry : TCPProtoTest.map.entrySet())
					if (entry.getKey().equals(key)) {
						value = new ObjectMapper().readValue((String) value, entry.getValue());
						break;
					}
				session.write(new TCPMessagePacket().addObject(key, value));
				if (key.equals("exit"))
					this.read = false;
			} catch (JsonParseException e) {
				System.err.println(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		this.connector.dispose();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		TCPPacket packet = (TCPPacket) message;
		System.out.println("##Received packet:");
		System.out.println("--magic: " + TCPPacket.MAGIC);
		System.out.println("--packetSize: " + packet.getPacketSize());
		System.out.println("--protocolVersion: " + packet.getProtocolVersion());
		System.out.println("--headerSize: " + packet.getHeaderSize());
		System.out.println("--payload: " + new String(packet.getPayload()));
		System.out.println("##END");
		System.out.println("##PAYLOAD:");
		System.out.println("--data: " + packet.getJsonData());
		System.out.println("--status:" + packet.getJsonStatus());
		System.out.println("##END");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		this.read = false;
	}
}
