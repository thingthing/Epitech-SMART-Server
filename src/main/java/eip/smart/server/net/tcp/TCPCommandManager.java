package eip.smart.server.net.tcp;

import java.io.IOException;
import java.util.HashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import eip.smart.server.Server;
import eip.smart.server.model.agent.TCPMessagePacket;

public class TCPCommandManager {
	private final static Logger	LOGGER	= LoggerFactory.getLogger(TCPCommandManager.class);

	private static void sendStatus(IoSession session, int code, String message) {
		session.write(new TCPMessagePacket().setStatus(code, message));
	}

	private HashMap<String, AgentCommandHandler<?>>	agentHandlers	= new HashMap<>();
	private HashMap<String, SessionCommandHandler>		sessionHandlers	= new HashMap<>();

	public TCPCommandManager() {
		for (SessionCommandList receptor : SessionCommandList.values())
			this.sessionHandlers.put(receptor.getKey(), receptor.getHandler());
		for (AgentCommandList receptor : AgentCommandList.values())
			this.agentHandlers.put(receptor.getKey(), receptor.getHandler());
	}

	public void handleTCPData(JsonNode data, IoSession session) throws JsonParseException, JsonMappingException, IOException {
		if (data.size() <= 0) {
			TCPCommandManager.sendStatus(session, 1, "no command");
			return;
		}

		JsonParser parser = data.traverse();
		while (parser.nextValue() != null)
			if (parser.getCurrentName() != null)
				this.searchHandler(parser.getCurrentName(), data.get(parser.getCurrentName()), session);
		parser.close();
	}

	private void searchHandler(String key, JsonNode value, IoSession session) {
		TCPCommandManager.LOGGER.debug("Received command {} with value {}", key, value);
		try {
			if (this.sessionHandlers.containsKey(key)) {
				TCPCommandManager.LOGGER.debug("Executing session command {}", key);
				this.sessionHandlers.get(key).handleCommand(value.asText(), session);
				TCPCommandManager.sendStatus(session, 0, "command " + key + " ok");
				TCPCommandManager.LOGGER.debug("Command {} executed", key);
			} else if (this.agentHandlers.containsKey(key)) {
				IoAgent ioAgent = Server.getServer().getIoAgentContainer().getBySession(session);
				if (ioAgent.getAgent() != null) {
					TCPCommandManager.LOGGER.debug("Executing agent command {}", key);
					this.agentHandlers.get(key).handleCommand(value.asText(), ioAgent.getAgent());
					TCPCommandManager.sendStatus(session, 0, "command " + key + " ok");
					TCPCommandManager.LOGGER.debug("Command {} executed", key);
				} else {
					TCPCommandManager.LOGGER.warn("Authentication required to execute agent command {}", key);
					TCPCommandManager.sendStatus(session, 1, "Authentication required to execute agent command " + key);
				}

			} else {
				TCPCommandManager.LOGGER.warn("Unknown command {}", key);
				TCPCommandManager.sendStatus(session, 1, "Unknown command " + key);
			}
		} catch (CommandException e) {
			TCPCommandManager.LOGGER.warn("Error during execution of command {} : {}", key, e.getMessage());
			TCPCommandManager.sendStatus(session, 1, "Error during execution of command " + key + " : " + e.getMessage());
		} catch (IOException e) {
			TCPCommandManager.LOGGER.error("Error during execution of command {}", key, e);
			TCPCommandManager.sendStatus(session, 1, "Error during execution of command " + key + " : " + e.getMessage());
		}
	}
}