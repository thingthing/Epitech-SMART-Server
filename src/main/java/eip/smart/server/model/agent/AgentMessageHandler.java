package eip.smart.server.model.agent;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AgentMessageHandler<T> {

	Class<T>	type;

	public AgentMessageHandler(Class<T> type) {
		this.type = type;
	}

	public void handleMessage(String data, AgentLogic agent) throws JsonParseException, JsonMappingException, IOException {
		this.handleMessage(new ObjectMapper().readValue(data, this.type), agent);
	}

	public abstract void handleMessage(T data, AgentLogic agent);
}