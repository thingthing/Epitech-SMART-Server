package eip.smart.server.net.tcp;

import org.apache.mina.core.session.IoSession;

public abstract class SessionCommandHandler<T> extends CommandHandler<T, IoSession> {

	public SessionCommandHandler(Class<T> type) {
		super(type);
	}

	@Override
	public abstract void innerHandleCommand(T data, IoSession ioAgent) throws CommandException;

}