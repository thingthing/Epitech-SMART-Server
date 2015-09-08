package eip.smart.server.net.tcp;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CommandHandler<T, U> {

	protected Class<T>	type;

	public CommandHandler(Class<T> type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public void handleCommand(JsonNode data, U u) throws JsonParseException, JsonMappingException, IOException, CommandException {
		this.innerHandleCommand((T) new ObjectMapper().reader().forType(this.type).readValue(data), u);
	}

	public abstract void innerHandleCommand(T t, U u) throws IOException, CommandException;
}
