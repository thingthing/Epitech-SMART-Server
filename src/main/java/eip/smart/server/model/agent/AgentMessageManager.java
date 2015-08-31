package eip.smart.server.model.agent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

public class AgentMessageManager {
	private HashMap<String, AgentMessageHandler<?>>	handlers	= new HashMap<>();

	public AgentMessageManager() {
		for (AgentMessageReceptor receptor : AgentMessageReceptor.values())
			this.addHandler(receptor.getKey(), receptor.getHandler());
	}

	private void addHandler(String key, AgentMessageHandler<?> handler) {
		this.handlers.put(key, handler);
	}

	public void handleMessage(JsonNode data, AgentLogic agent) throws JsonParseException, JsonMappingException, IOException {
		if (data.size() <= 0) {
			agent.sendStatus(1, "no command");
			return;
		}

		JsonParser parser = data.traverse();
		while (parser.nextValue() != null) {
			if (parser.getCurrentName() == null)
				continue;

			String key = parser.getCurrentName();
			if (key == "name")
				continue;
			String value = data.get(parser.getCurrentName()).asText();
			boolean unknown = true;
			for (Entry<String, AgentMessageHandler<?>> entry : this.handlers.entrySet())
				if (entry.getKey().equals(key)) {
					unknown = false;
					entry.getValue().handleMessage(value, agent);
					break;
				}
			if (unknown)
				agent.sendStatus(1, "unknown command " + key);
		}
		parser.close();
	}
}