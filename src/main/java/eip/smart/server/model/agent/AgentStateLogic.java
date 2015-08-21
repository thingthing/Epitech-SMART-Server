package eip.smart.server.model.agent;

import java.time.Instant;
import java.util.Date;

import eip.smart.cscommons.model.agent.AgentState;
import eip.smart.cscommons.model.geometry.v2.Point3D;

/**
 * <b>AgentState is the enum allowing the management of the Agents'states.</b>
 *
 * @author Pierre Demessence
 * @version 1.0
 */
public enum AgentStateLogic {
	/**
	 * this state has to be activated by an agent's message
	 * this state is locked
	 */
	BLOCKED(new AgentStateHandler(true) {

		@Override
		public boolean canMove() {
			return false;
		}

		@Override
		public void doAction(AgentLogic agent) {}
	}),

	/**
	 * this state has to be activated by an agent's message
	 * this state is locked
	 */
	DERANGED(new AgentStateHandler(true) {

		@Override
		public boolean canMove() {
			return false;
		}

		@Override
		public void doAction(AgentLogic agent) {}
	}),

	/**
	 * this state has to be activated by the NO_SIGNAL state
	 */
	LOST_SIGNAL(new AgentStateHandler() {

		@Override
		public boolean canMove() {
			return false;
		}
	}),

	/**
	 * this state has to be activated by an agent's message
	 */
	LOW_BATTERY(new AgentStateHandler(true) {

		@Override
		public boolean canMove() {
			return false;
		}

		@Override
		public void doAction(AgentLogic agent) {
			agent.recall();
			agent.setState(AgentState.RECALL_BATTERY);
			// switch agent status to RECALL_BATTERY
		}
	}),

	/**
	 * this state is decided by server (not checked)
	 * this state is locked
	 */
	NO_BATTERY(new AgentStateHandler(true) {

		@Override
		public boolean canMove() {
			return false;
		}

		@Override
		public void doAction(AgentLogic agent) {}

		@Override
		public int getPriority() {
			return (1);
		}
	}),

	/**
	 * this state is checked by the server
	 */
	NO_SIGNAL(new AgentStateHandler() {
		private int	cpt	= 4;

		@Override
		public boolean canMove() {
			return false;
		}

		@Override
		public boolean checkState(AgentLogic agent) {
			if (Date.from(Instant.now()).getTime() - agent.getLastContact().getTime() > 5 * 60 * 1000)
				return true;
			return false;
		}

		@Override
		public void doAction(AgentLogic agent) {
			if (agent.isConnected())
				agent.setState(AgentState.OK);
			if (++this.cpt == 20)
				agent.setState(AgentState.LOST_SIGNAL);
		}
	}),

	/**
	 * this state is the default state
	 */
	OK(new AgentStateHandler() {
		@Override
		public boolean canMove() {
			return true;
		}

		// default value
		@Override
		public boolean checkState(AgentLogic agent) {
			return true;
		}

		@Override
		// do all the normal agent actions
		public void doAction(AgentLogic agent) {

		}

		@Override
		public int getPriority() {
			return (-1);
		}

		@Override
		public boolean isLocked() {
			return (false);
		}
	}),

	/**
	 * this state is activated by a server's decision
	 */
	RECALL(new AgentStateHandler() {
		@Override
		public boolean canMove() {
			return true;
		}

		@Override
		public void doAction(AgentLogic agent) {
			agent.recall();
		}

		@Override
		public int getPriority() {
			return (1);
		}
	}),

	/**
	 * this state is activated by the LOW_BATTERY state
	 */
	RECALL_BATTERY(new AgentStateHandler() {
		// the agent is coming to the base because it has not enough battery, he can't receive orders
		@Override
		public boolean canMove() {
			return false;
		}

		@Override
		public void doAction(AgentLogic agent) {
			agent.recall();
		}

		@Override
		public int getPriority() {
			return (1);
		}

	}),

	/**
	 * this state has to be activated by an agent's message
	 * this state is locked
	 */
	RECALL_ERROR(new AgentStateHandler(true) {

		@Override
		public boolean canMove() {
			return false;
		}

		@Override
		public void doAction(AgentLogic agent) {}
	}),

	/**
	 * this state is checked by the server
	 */
	STILL(new AgentStateHandler() {
		@Override
		public boolean canMove() {
			return true;
		}

		@Override
		public boolean checkState(AgentLogic agent) {
			boolean still = true;
			int check_size = 10;
			int i = 0;

			while (i < ((agent.getPositions().size() > check_size) ? check_size : agent.getPositions().size())) {
				if (agent.getCurrentPosition() != agent.getPositions().get(i) && still)
					still = false;
				i++;
			}
			if (still)
				return true;
			return false;
		}

		@Override
		public void doAction(AgentLogic agent) {
			Point3D currentPos = agent.getPositions().get(agent.getPositions().size() - 1);
			Point3D order = new Point3D(currentPos.getX() + 5, currentPos.getY() + 5, 0);
			agent.pushOrder(order);
		}

		@Override
		public int getPriority() {
			return (4);
		}
	}),

	/**
	 * this state is checked by the server
	 */
	STILL_ERROR(new AgentStateHandler() {
		@Override
		public boolean canMove() {
			return true;
		}

		@Override
		public boolean checkState(AgentLogic agent) {
			boolean still = true;
			int check_size = 50;
			int i = 0;

			while (i < ((agent.getPositions().size() > check_size) ? check_size : agent.getPositions().size())) {
				if (agent.getCurrentPosition() != agent.getPositions().get(i) && still)
					still = false;
				i++;
			}
			if (still)
				return true;
			return false;
		}

		@Override
		public void doAction(AgentLogic agent) {
			Point3D currentPos = agent.getPositions().get(agent.getPositions().size() - 1);
			Point3D order = new Point3D(currentPos.getX() + 5, currentPos.getY() + 5, 0);
			agent.pushOrder(order);
		}

		@Override
		public int getPriority() {
			return (5);
		}
	}),

	/**
	 * this state has to be activated by an agent's message
	 * this state is locked
	 */
	UNKNOWN_ERROR(new AgentStateHandler(true) {

		@Override
		public boolean canMove() {
			return false;
		}

		@Override
		public void doAction(AgentLogic agent) {}
	});

	private AgentStateHandler	handler;

	AgentStateLogic(AgentStateHandler status) {
		this.handler = status;
	}

	public AgentStateHandler getHandler() {
		return this.handler;
	}

	/**
	 * @return
	 * @see eip.smart.server.model.agent.AgentStateHandler#isLocked()
	 */
	public boolean isLocked() {
		return this.handler.isLocked();
	}
}