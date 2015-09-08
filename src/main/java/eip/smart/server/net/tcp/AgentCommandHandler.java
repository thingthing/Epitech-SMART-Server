package eip.smart.server.net.tcp;

import eip.smart.server.model.agent.AgentLogic;

public abstract class AgentCommandHandler<T> extends CommandHandler<T, AgentLogic> {

	public AgentCommandHandler(Class<T> type) {
		super(type);
	}

	@Override
	public abstract void innerHandleCommand(T data, AgentLogic agent) throws CommandException;
}