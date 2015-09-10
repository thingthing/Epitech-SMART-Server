package eip.smart.server.model.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eip.smart.cscommons.model.agent.AgentState;

public class AgentStateManager {

	private final static Logger						LOGGER	= LoggerFactory.getLogger(AgentStateManager.class);

	private AgentLogic								agent;

	private HashMap<AgentState, AgentStateLogic>	states	= new HashMap<>();

	public AgentStateManager(AgentLogic agent) {
		this.agent = agent;
		for (AgentState state : AgentState.values()) {
			AgentStateLogic logic = AgentStateLogic.valueOf(state.name());
			if (logic != null)
				this.states.put(state, logic);
			else
				AgentStateManager.LOGGER.warn("Missing logic for state {}", state);

		}
		for (AgentStateLogic logic : AgentStateLogic.values())
			if (!this.states.containsValue(logic))
				AgentStateManager.LOGGER.warn("Missing state");

	}

	public void doAction() {
		this.states.get(this.agent.getState()).getHandler().doAction(this.agent);
	}

	private AgentState getByLogic(AgentStateLogic logic) {
		for (Entry<AgentState, AgentStateLogic> entry : this.states.entrySet())
			if (entry.getValue() == logic)
				return entry.getKey();
		return (null);
	}

	public void updateState() {
		if (this.states.get(this.agent.getState()).getHandler().isLocked())
			return;
		AgentState res = this.agent.getState();

		List<AgentStateLogic> list = new ArrayList<>(this.states.values());
		Collections.sort(list, new Comparator<Object>() {
			@Override
			public int compare(Object obj1, Object obj2) {
				AgentStateLogic as1 = (AgentStateLogic) obj1;
				AgentStateLogic as2 = (AgentStateLogic) obj2;
				int lib1 = as1.getHandler().getPriority();
				int lib2 = as2.getHandler().getPriority();
				if (lib1 > lib2)
					return (-1);
				else if (lib1 == lib2)
					return (0);
				return (1);
			}
		});

		for (AgentStateLogic logic : list)
			if (logic.getHandler().checkState(this.agent))
				res = this.getByLogic(logic);
		this.agent.setState(res);
	}
}
