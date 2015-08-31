package eip.smart.server.net.tcp;

import java.io.IOException;

public interface CommandHandler<T, U> {

	public abstract void handleCommand(T t, U u) throws IOException, CommandException;
}
