package eip.smart.server.net.tcp;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eip.smart.server.model.agent.AgentLogic;

public abstract class AgentCommandHandler<T> implements CommandHandler<T, AgentLogic> {

	Class<T>	type;

	public AgentCommandHandler(Class<T> type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public void handleCommand(JsonNode data, AgentLogic agent) throws JsonParseException, JsonMappingException, IOException, CommandException {
		this.handleCommand((T) new ObjectMapper().reader().forType(this.type).readValue(data), agent);
	}

	@Override
	public abstract void handleCommand(T data, AgentLogic agent) throws CommandException;
}