package eip.smart.server.net;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class BroadcastUDPHandler implements IoHandler {

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String msg = (String) message;
		System.out.println("Received " + (String) message + " by " + session.getRemoteAddress());
		if (msg.equals("hello"))
			session.write("world");
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {}

	@Override
	public void sessionClosed(IoSession session) throws Exception {}

	@Override
	public void sessionCreated(IoSession session) throws Exception {}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {}

	@Override
	public void sessionOpened(IoSession session) throws Exception {}

}
