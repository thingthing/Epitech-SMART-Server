package eip.smart.server.net.tcp;

import org.apache.mina.core.session.IoSession;

public interface SessionCommandHandler extends CommandHandler<String, IoSession> {

	@Override
	public abstract void handleCommand(String data, IoSession ioAgent) throws CommandException;

}