package eip.smart.model;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Pierre Demessence on 10/10/2014.
 */
public class Modeling {

	private final static Logger	LOGGER	= Logger.getLogger(Modeling.class.getName());

	private ArrayList<Area>		areas	= new ArrayList<Area>();
	private ArrayList<Agent>	agents	= new ArrayList<Agent>();
	private ArrayList<Client>	clients	= new ArrayList<Client>();

	public Modeling() {
		this.areas.add(new Area());
		this.areas.add(new Area());

		Agent n1 = new Agent();
		n1.setDestination(new Point(-6, -6, 8));
		this.agents.add(n1);

		Agent n2 = new Agent();
		n2.setDestination(new Point(7, -4, 0));
		this.agents.add(n2);
	}

	public void dumpAgents() {
		Modeling.LOGGER.log(Level.INFO, "Dumping Agents");
		for (Agent a : this.agents) {
			Modeling.LOGGER.log(Level.INFO, "Agent " + a.getID() + " :");
			Modeling.LOGGER.log(Level.INFO, "--Position : " + a.getCurrentPosition());
			Modeling.LOGGER.log(Level.INFO, "--Destination : " + a.getDestination());
		}
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

	private void handleAGentsState() {
		Modeling.LOGGER.log(Level.INFO, "-->Handling Agents State...");
	}

	private void moveAgents() {
		Modeling.LOGGER.log(Level.INFO, "-->Moving Agents... (SIMULATION)");
		for (Agent a : this.agents)
			a.move();
	}

	public void run() {
		Modeling.LOGGER.log(Level.INFO, "Running Modeling");
		this.updateAgents();
		this.updateAreaAgentsAttributed();
		this.updateAgentsDestination();
		this.updateAgentsOrders();
	}

	/**
	 * Envoie les informations aux clients.
	 */
	public void sendData() {

	}

	private void updateAgents() {
		Modeling.LOGGER.log(Level.INFO, "->Updating Agents...");
		this.moveAgents();
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
