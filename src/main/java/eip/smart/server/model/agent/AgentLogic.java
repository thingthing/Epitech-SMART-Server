package eip.smart.server.model.agent;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.fasterxml.jackson.databind.JsonNode;

import eip.smart.cscommons.model.agent.Agent;
import eip.smart.cscommons.model.agent.AgentState;
import eip.smart.cscommons.model.agent.AgentType;
import eip.smart.cscommons.model.geometry.Point3D;
import eip.smart.cscommons.model.modeling.Area;

public class AgentLogic extends Agent {

	public interface sendMessageCallback {
		public void callback(Object message);
	}

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
	private AgentMessageManager	messageManager	= new AgentMessageManager();

	private AgentStateManager	stateManager	= new AgentStateManager();

	public AgentLogic(Agent agent) {
		super(agent);
	}

	/**
	 * default constructor and create handlers
	 */
	public AgentLogic(String name) {
		super(name);
	}

	/**
	 * Add a Point at the agent's list of orders
	 *
	 * @param order
	 *            Point, new order send to the agent
	 */
	@SuppressWarnings("unchecked")
	public void pushOrder(Point3D order) {
		this.orders.add(0, order);
		this.sendMessage(new ImmutablePair<>("order", order));
	}

	/**
	 * Send to the agent the order of going back to it depart's point.
	 */
	public void recall() {
		this.pushOrder(new Point3D(0, 0, 0));
	}

	/**
	 * Check if the message has been received by the agent
	 *
	 * @param data
	 *            String, the message
	 */
	public void receiveMessage(JsonNode data) {
		try {
			this.messageManager.handleMessage(data, this);
		} catch (IOException e) {
			this.sendStatus(1, e.getMessage());
		}

	}

	/**
	 * Send a message to the dashboard
	 *
	 * @param objects
	 *            one or many objects that will be send
	 */
	@SuppressWarnings("unchecked")
	public void sendMessage(ImmutablePair<String, Object>... objects) {
		TCPMessagePacket message = new TCPMessagePacket();
		for (ImmutablePair<String, Object> p : objects)
			message.addObject(p.getKey(), p.getValue());
		if (this.messageCallback != null)
			this.messageCallback.callback(message);
	}

	public void sendStatus(int statusCode, String statusMessage) {
		TCPMessagePacket message = new TCPMessagePacket();
		message.setStatus(statusCode, statusMessage);
		if (this.messageCallback != null)
			this.messageCallback.callback(message);
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public void setCurrentBearing(Double bearing) {
		this.bearings.add(0, bearing);
	}

	public void setCurrentPosition(Point3D position) {
		this.positions.add(0, position);
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
		this.stateManager.updateState(this);
	}

}