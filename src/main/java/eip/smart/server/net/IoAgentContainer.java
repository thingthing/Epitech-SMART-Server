package eip.smart.server.net;

import java.util.ArrayList;

import org.apache.mina.core.session.IoSession;

import eip.smart.model.Agent;

public class IoAgentContainer extends ArrayList<IoAgent> {

	public void addAgent(Agent agent) {
		this.add(new IoAgent(agent));
	}

	public void addSession(IoSession session) {
		this.add(new IoAgent(session));
	}

	public ArrayList<Agent> getAgents() {
		ArrayList<Agent> agents = new ArrayList<>();
		for (IoAgent ioAgent : this)
			if (ioAgent.getAgent() != null)
				agents.add(ioAgent.getAgent());
		return (agents);
	}

	public IoAgent getByAgent(Agent agent) {
		for (IoAgent ioAgent : this)
			if (ioAgent.getAgent() == agent)
				return (ioAgent);
		return (null);
	}

	public IoAgent getByAgentName(String name) {
		for (IoAgent ioAgent : this)
			if (ioAgent.getAgent() != null && ioAgent.getAgent().getName().equals(name))
				return (ioAgent);
		return (null);
	}

	public IoAgent getBySession(IoSession session) {
		for (IoAgent ioAgent : this)
			if (ioAgent.getSession() == session)
				return (ioAgent);
		return (null);
	}

	public ArrayList<IoSession> getSessions() {
		ArrayList<IoSession> sessions = new ArrayList<>();
		for (IoAgent ioAgent : this)
			if (ioAgent.getSession() != null)
				sessions.add(ioAgent.getSession());
		return (sessions);
	}

}
