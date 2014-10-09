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
}
