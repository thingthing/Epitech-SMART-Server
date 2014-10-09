package eip.smart.model;

import java.util.Date;

/**
 * Created by Pierre Demessence on 09/10/2014.
 */
public class Agent {
    int         ID;
    AgentType   type;
    Point       coordinates;
    Date        lastContact;
    Point       destination;

    /**
     * Created by Pierre Demessence on 09/10/2014.
     */
    public static enum AgentType {
        TERRESTRIAL, AERIAL, SUBMARINE;
    }
}
