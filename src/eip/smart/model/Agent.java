package eip.smart.model;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Pierre Demessence on 09/10/2014.
 */
public class Agent {
    static int nextID = 1;
    private int ID = -1;
    private AgentType type = AgentType.TERRESTRIAL;
    private AgentState state = AgentState.OK;
    private LinkedList<Point> positions = new LinkedList<Point>();
    private LinkedList<Point> orders = new LinkedList<Point>();
    private Point destination = new Point(0, 0, 0);
    private Date lastContact = Date.from(Instant.now());

    public Agent() {
        this.ID = Agent.nextID++;
        this.setCurrentPosition(new Point(0, 0, 0));
    }

    public Point getCurrentOrder() {
        return (orders.peek());
    }

    public void pushOrder(Point order) {
        this.orders.push(order);
    }

    public int getID() {
        return ID;
    }

    public AgentType getType() {
        return type;
    }

    public void setType(AgentType type) {
        this.type = type;
    }

    public AgentState getState() {
        return state;
    }

    public void setState(AgentState state) {

        this.state = state;
    }

    public Point getCurrentPosition() {
        return (positions.peek());
    }

    public void setCurrentPosition(Point position) {

        this.positions.push(position);
    }

    public Point getDestination() {
        return destination;
    }

    public void setDestination(Point destination) {
        this.destination = destination;
    }

    public Date getLastContact() {
        return lastContact;
    }

    public void setLastContact(Date lastContact) {
        this.lastContact = lastContact;
    }

    public void move() {
        double newx;
        double newy;
        double newz;

        newx = Math.min(1, Math.max(-1, this.getDestination().getX() - this.getCurrentPosition().getX()));
        newy = Math.min(1, Math.max(-1, this.getDestination().getY() - this.getCurrentPosition().getY()));
        newz = Math.min(1, Math.max(-1, this.getDestination().getZ() - this.getCurrentPosition().getZ()));

        this.setCurrentPosition(Point.add(this.getCurrentPosition(), new Point(newx, newy, newz)));
    }

    public void updateState() {
        if (Date.from(Instant.now()).getTime() - this.lastContact.getTime() > 5 * 60 * 1000)
            this.state = AgentState.LOST;
    }

    public static enum AgentType {
        TERRESTRIAL, AERIAL, SUBMARINE;
    }

    public static enum AgentState {
        OK, LOST, STILL, NO_RETURN, LOW_BATTERY, NO_BATTERY, UNKNOWN_ERROR
    }
}
