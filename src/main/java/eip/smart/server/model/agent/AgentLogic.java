package eip.smart.server.model.agent;

import java.util.Date;

import org.apache.commons.lang3.tuple.ImmutablePair;

import eip.smart.cscommons.configuration.Configuration;
import eip.smart.cscommons.model.agent.Agent;
import eip.smart.cscommons.model.agent.AgentState;
import eip.smart.cscommons.model.agent.AgentType;
import eip.smart.cscommons.model.geometry.Point3D;
import eip.smart.cscommons.model.modeling.Area;

@SuppressWarnings("unchecked")
public class AgentLogic extends Agent {

	public interface sendMessageCallback {
		public void callback(Object message);
	}

	private static int getPosMax() {
		return new Configuration("server").getPropertyInteger("AGENTS_MAX_POS");
	}

	private Point3D				lastOrder		= null;

	/**
	 * Object (sendMessageCallback), managing the messages'sending
	 *
	 * @see sendMessageCallback
	 */
	private sendMessageCallback	messageCallback	= null;

	/**
	 * Object (AgentMessageManager), managing the messages'reception
	 *
	 * @see AgentMessageManager
	 */
	private AgentStateManager	stateManager	= new AgentStateManager(this);

	public AgentLogic(Agent agent) {
		super(agent);
	}

	/**
	 * default constructor and create handlers
	 */
	public AgentLogic(String name) {
		super(name);
	}

	public void clearOrders() {
		this.orders.clear();
	}

	public void connect() {
		this.connected = true;
		this.lastOrder = null;
	}

	public Point3D getLastOrder() {
		return this.lastOrder;
	}

	/**
	 * Add a Point at the agent's list of orders
	 *
	 * @see Point
	 * @param order
	 *            Point, new order send to the agent
	 */
	public void newOrder(Point3D order) {
		this.clearOrders();
		this.pushOrder(order);
	}

	/**
	 * Add a Point at the agent's list of orders
	 *
	 * @param order
	 *            Point, new order send to the agent
	 */
	public void pushOrder(Point3D order) {
		this.orders.add(Math.max(0, this.orders.size()), order);
		this.sendOrder(order);
	}

	/**
	 * Send to the agent the order of going back to it depart's point.
	 */
	public void recall() {
		this.pushOrder(new Point3D(0, 0, 0));
	}

	public void run() {
		this.stateManager.doAction();
	}

	/**
	 * Send a message to the dashboard
	 *
	 * @param objects
	 *            one or many objects that will be send
	 */
	public void sendMessage(ImmutablePair<String, Object>... objects) {
		TCPMessagePacket message = new TCPMessagePacket();
		for (ImmutablePair<String, Object> p : objects)
			message.addObject(p.getKey(), p.getValue());
		if (this.messageCallback != null)
			this.messageCallback.callback(message);
	}

	public void sendOrder(Point3D order) {
		if (order.equals(this.lastOrder))
			return;
		this.lastOrder = order;
		this.sendMessage(new ImmutablePair<>("order", order));
	}

	public void sendStatus(int statusCode, String statusMessage) {
		TCPMessagePacket message = new TCPMessagePacket();
		message.setStatus(statusCode, statusMessage);
		if (this.messageCallback != null)
			this.messageCallback.callback(message);
	}

	public void setBattery(double battery) {
		this.battery = battery;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public void setCurrentBearing(Double bearing) {
		this.bearings.add(0, bearing);
	}

	public void setCurrentDestination(Point3D dest) {
		this.currentDestination = dest;
	}

	public void setCurrentPosition(Point3D position) {
		if (!this.positions.isEmpty() && this.getCurrentPosition().equals(position))
			return;
		this.positions.put(new Date(), position);
		if (this.positions.size() > AgentLogic.getPosMax())
			this.positions.remove(this.positions.lastKey());
	}

	public void setDestination(Area destination) {
		this.destination = destination;
	}

	public void setLastContact(Date lastContact) {
		this.lastContact = lastContact;
	}

	public void setSendMessageCallback(sendMessageCallback messageCallback) {
		this.messageCallback = messageCallback;
	}

	public void setState(AgentState state) {
		this.state = state;
	}

	public void setType(AgentType type) {
		this.type = type;
	}

	/**
	 * Update the Agent's state, using it attributes "position" and "lastContact"
	 */
	public void updateState() {
		this.stateManager.updateState();
	}

}