package eip.smart.model;

import java.util.ArrayList;

/**
 * Created by Pierre Demessence on 10/10/2014.
 */
public class Modeling {
    private ArrayList<Area> areas = new ArrayList<Area>();
    private ArrayList<Agent> agents = new ArrayList<Agent>();
    private ArrayList<Client> clients = new ArrayList<Client>();

    public Modeling() {
        this.areas.add(new Area());

        Agent n1 = new Agent();
        n1.setDestination(new Point(0, 0, 0));
        this.agents.add(n1);

        Agent n2 = new Agent();
        n1.setDestination(new Point(7, -4, 0));
        this.agents.add(n2);
    }

    public void run() {
        System.out.println("Running Modeling");
        this.updateAgents();
        this.updateAreaAgentsAttributed();
        this.updateAgentsDestination();
        this.updateAgentsOrders();
    }

    private void updateAgents() {
        System.out.println("->Updating Agents...");
        this.moveAgents();
        this.updateAgentsState();
        this.handleAGentsState();
    }

    public void dumpAgents() {
        System.out.println("Dumping Agents");
        for (Agent a : this.agents) {
            System.out.println("Agent "+a.getID()+" :");
            System.out.println("--Position : "+a.getCurrentPosition());
            System.out.println("--Destination : "+a.getDestination());
        }
    }

    private void moveAgents() {
        System.out.println("-->Moving Agents... (SIMULATION)");
        for (Agent a : this.agents)
            a.move();
    }

    private void updateAgentsState() {
        System.out.println("-->Updating Agents State...");
    }

    private void handleAGentsState() {
        System.out.println("-->Handling Agents State...");
    }

    private void updateAreaAgentsAttributed() {
        for (Area a : this.areas)
            a.updateCompletion();
        System.out.println("->Attributing number of agents for each area...");
    }

    private void updateAgentsDestination() {
        System.out.println("->Updating destination for each agent...");
    }

    private void updateAgentsOrders() {
        System.out.println("->Updating orders for each agent...");
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

    /**
     * Envoie les informations aux clients.
     */
    public void sendData() {

    }

}
