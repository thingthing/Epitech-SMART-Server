package eip.smart.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Pierre Demessence on 10/10/2014.
 */
@SuppressWarnings("static-method")
public class Modeling implements Serializable {

	private final static Logger	LOGGER	= Logger.getLogger(Modeling.class.getName());

	private static int			nextID	= 1;
	private int					ID		= -1;
	private String				name;
	private ArrayList<Area>		areas	= new ArrayList<>();
	private ArrayList<Agent>	agents	= new ArrayList<>();
	private long				tick	= 0;

	public Modeling(String name) {
		this.ID = Modeling.nextID++;
		this.name = name;
		this.areas.add(new Area());
		this.areas.add(new Area());
	}

	public void addAgent(Agent agent) {
		this.agents.add(agent);
	}

	public void dumpAgents() {
		Modeling.LOGGER.log(Level.INFO, "Dumping Agents");
		for (Agent a : this.agents) {
			Modeling.LOGGER.log(Level.INFO, "Agent " + a.getID() + " :");
			Modeling.LOGGER.log(Level.INFO, "--Position : " + a.getCurrentPosition());
			Modeling.LOGGER.log(Level.INFO, "--Destination : " + a.getDestination());
		}
	}

	public ArrayList<Agent> getAgents() {
		return (this.agents);
	}

	public ArrayList<Area> getAreas() {
		return (this.areas);
	}

	public double getCompletion() {
		double res = 0;

		if (this.areas.size() == 0)
			return (100.0d);

		for (Area a : this.areas)
			res += a.getCompletion();
		res /= this.areas.size();
		return (res);
	}

	public int getID() {
		return (this.ID);
	}

	public String getName() {
		return (this.name);
	}

	public long getTick() {
		return (this.tick);
	}

	private void handleAGentsState() {
		Modeling.LOGGER.log(Level.INFO, "-->Handling Agents State...");
	}

	public void run() {
		++this.tick;
		Modeling.LOGGER.log(Level.INFO, "Modeling (" + this.name + ") running (tick " + this.tick + ")");
		this.updateAgents();
		this.updateAreaAgentsAttributed();
		this.updateAgentsDestination();
		this.updateAgentsOrders();
	}

	private void updateAgents() {
		Modeling.LOGGER.log(Level.INFO, "->Updating Agents...");
		this.updateAgentsState();
		this.handleAGentsState();
	}

	private void updateAgentsDestination() {
		Modeling.LOGGER.log(Level.INFO, "->Updating destination for each agent...");
	}

	private void updateAgentsOrders() {
		Modeling.LOGGER.log(Level.INFO, "->Updating orders for each agent...");
	}

	private void updateAgentsState() {
		Modeling.LOGGER.log(Level.INFO, "-->Updating Agents State...");
	}

	private void updateAreaAgentsAttributed() {
		for (Area a : this.areas)
			a.updateCompletion();
		Modeling.LOGGER.log(Level.INFO, "->Attributing number of agents for each area...");
	}

}
