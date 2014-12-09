package eip.smart.model;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;

import eip.smart.model.geometry.Point;
import eip.smart.model.proxy.SimpleAgentProxy;

/**
 * Created by Pierre Demessence on 09/10/2014.
 */
public class Agent {

	public static enum AgentState {
		OK, LOST, STILL, NO_RETURN, LOW_BATTERY, NO_BATTERY, UNKNOWN_ERROR
	}

	public static enum AgentType {
		TERRESTRIAL, AERIAL, SUBMARINE;
	}

	static int					nextID		= 1;

	private int					ID			= -1;
	private AgentType			type		= AgentType.TERRESTRIAL;
	private AgentState			state		= AgentState.OK;
	private LinkedList<Point>	positions	= new LinkedList<>();
	private LinkedList<Point>	orders		= new LinkedList<>();
	private Area				destination	= null;

	private Date				lastContact	= Date.from(Instant.now());

	public Agent() {
		this.ID = Agent.nextID++;
		this.setCurrentPosition(new Point(0, 0, 0));
	}

	public Point getCurrentOrder() {
		return (this.orders.peek());
	}

	public Point getCurrentPosition() {
		return (this.positions.peek());
	}

	public Area getDestination() {
		return (this.destination);
	}

	public int getID() {
		return (this.ID);
	}

	public Date getLastContact() {
		return (this.lastContact);
	}

	public LinkedList<Point> getPositions() {
		return (this.positions);
	}

	public SimpleAgentProxy getProxy() {
		return (new SimpleAgentProxy(this));
	}

	public AgentState getState() {
		return (this.state);
	}

	public AgentType getType() {
		return (this.type);
	}

	public void pushOrder(Point order) {
		this.orders.push(order);
	}

	public void setCurrentPosition(Point position) {
		this.positions.push(position);
	}

	public void setDestination(Area destination) {
		this.destination = destination;
	}

	public void setLastContact(Date lastContact) {
		this.lastContact = lastContact;
	}

	public void setState(AgentState state) {

		this.state = state;
	}

	public void setType(AgentType type) {
		this.type = type;
	}

	public void updateState() {
		if (Date.from(Instant.now()).getTime() - this.lastContact.getTime() > 5 * 60 * 1000)
			this.state = AgentState.LOST;
	}
}
