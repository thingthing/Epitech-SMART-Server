package eip.smart.server;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.session.IoSession;

import eip.smart.server.model.agent.AgentLogic;
import eip.smart.server.net.tcp.IoAgentContainer;

public class ServerAgentManager {

	private ScheduledExecutorService	executorService		= Executors.newSingleThreadScheduledExecutor();
	/**
	 * The ioAgentContainer to store Agents and bound TCP sessions.
	 */
	private IoAgentContainer			ioAgentContainer	= new IoAgentContainer();

	private Future<?>					task				= null;

	public ServerAgentManager() {
		this.task = this.executorService.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				for (AgentLogic agent : ServerAgentManager.this.getAgentsAvailable()) {
					if (agent.getCurrentDestination() != null && agent.getCurrentDestination().equals(agent.getCurrentPosition()))
						agent.setCurrentDestination(null);
					if (!agent.getOrders().isEmpty() && agent.getCurrentOrder().equals(agent.getCurrentPosition()))
						agent.popCurrentOrder();
					agent.updateState();
					agent.run();
				}
			}
		}, 0, 1000, TimeUnit.MILLISECONDS);
	}

	public AgentLogic getAgentByName(String name) {
		if (name != null)
			for (AgentLogic a : this.getAgentsAvailable())
				if (name.equals(a.getName()))
					return (a);
		return null;
	}

	/**
	 * Get all the connected agents.
	 *
	 * @return
	 */
	public List<AgentLogic> getAgentsAvailable() {
		return (this.ioAgentContainer.getAgents());
	}

	/**
	 * Get the IoAgentContainer which store the Agents and TCP sessions.
	 *
	 * @return
	 */
	public IoAgentContainer getIoAgentContainer() {
		return (this.ioAgentContainer);
	}

	/**
	 * Get all the connected TCP sessions.
	 *
	 * @return
	 */
	public List<IoSession> getSessions() {
		return (this.ioAgentContainer.getSessions());
	}

	public void stop() {
		this.task.cancel(true);
	}
}
