package eip.smart.server.net;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import eip.smart.model.Agent;

public class AgentServerHandler implements IoHandler {

	private HashMap<IoSession, String>	sessions	= new HashMap<>();

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String str = message.toString();
		if (str.trim().equalsIgnoreCase("quit")) {
			session.close(true);
			return;
		}
		if (str.trim().matches("/login [a-zA-Z0-9]*")) {
			this.sessions.put(session, str.trim().substring(7));
			session.write("#SERVER: You are now : " + this.sessions.get(session));
			return;
		}
		if (this.sessions.get(session).equals("")) {
			session.write("#SERVER: Use /login to get a name.");
			return;
		}

		for (Entry<IoSession, String> ioSession : this.sessions.entrySet())
			if (ioSession.getKey() != session)
				ioSession.getKey().write(this.sessions.get(session) + " : " + message);
		System.out.println(this.sessions.get(session) + " : " + message);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		this.sessions.remove(session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		this.sessions.put(session, "");
		session.write(new Agent());
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		System.out.println("IDLE " + session.getIdleCount(status));
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
	}

}
