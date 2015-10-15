package eip.smart.server.net.tcp;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eip.smart.server.Server;

public enum SessionCommandList {

	EXIT("exit", new SessionCommandHandler<Byte>(Byte.class) {
		@Override
		public void innerHandleCommand(Byte data, IoSession session) {
			session.close(true);
			SessionCommandList.LOGGER.info("Session closed remotely by {} with exit code {}", session.getRemoteAddress(), data);
		}
	}),
	NAME("name", new SessionCommandHandler<String>(String.class) {
		@Override
		public void innerHandleCommand(String data, IoSession session) throws CommandException {

			String name = data.trim();
			if (name.isEmpty())
				throw new CommandException("Invalid name " + name);

			IoAgentContainer ioAgentContainer = Server.getServer().getAgentManager().getIoAgentContainer();
			if (ioAgentContainer.getBySession(session).getAgent() != null)
				throw new CommandException("Already connected");
			IoAgent ioAgent = ioAgentContainer.getByAgentName(name);
			if (ioAgent != null) {
				if (ioAgent.getAgent().isConnected())
					throw new CommandException("Name " + name + " already used");
				ioAgentContainer.remove(ioAgentContainer.getBySession(session));
				ioAgent.sessionCreated(session);
			} else
				ioAgentContainer.getBySession(session).createAgent(name);
		}
	});

	private final static Logger			LOGGER	= LoggerFactory.getLogger(SessionCommandList.class);

	private SessionCommandHandler<?>	handler;
	private String						key;

	private SessionCommandList(String key, SessionCommandHandler<?> handler) {
		this.key = key;
		this.handler = handler;
	}

	/**
	 * @return the handler
	 */
	public SessionCommandHandler<?> getHandler() {
		return this.handler;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return this.key;
	}

}
