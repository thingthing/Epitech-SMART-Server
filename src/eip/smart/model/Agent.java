package eip.smart.model;

import java.util.Date;

/**
 * Created by Pierre Demessence on 09/10/2014.
 */
public class Agent {
    private int         ID;
    private AgentType   type;
    private Point       position;
    private Point       destination;
    private Date        lastContact;

    /**
     * Created by Pierre Demessence on 09/10/2014.
     */
    public static enum AgentType {
        TERRESTRIAL, AERIAL, SUBMARINE;
    }

    public Agent(int ID, AgentType type, Point position, Point destination, Date lastContact) {
        this.ID = ID;
        this.type = type;
        this.position = position;
        this.destination = destination;
        this.lastContact = lastContact;
    }
}
