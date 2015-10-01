package eip.smart.server.model.modeling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eip.smart.cscommons.model.modeling.ModelingState;

public class ModelingTask implements Runnable {

	private final static Logger	LOGGER		= LoggerFactory.getLogger(ModelingTask.class);
	private static final int	RUN_DELAY	= 1000;

	private ModelingLogic		modeling	= null;
	private Object				o			= new Object();
	private volatile boolean	paused		= false;
	private boolean				running		= true;

	/**
	 * Creates a new ModelingTask using given argument as current Modeling
	 *
	 * @param modeling
	 */
	public ModelingTask(ModelingLogic modeling) {
		this.modeling = modeling;
		this.modeling.setState(ModelingState.RUNNING);
	}

	/**
	 * @return true if the Modeling is paused
	 */
	public boolean isPaused() {
		return this.paused;
	}

	/**
	 * Pauses the modeling
	 */
	public void pause() {
		this.paused = true;
		this.modeling.setState(ModelingState.PAUSED);
	}

	/**
	 * Resumes the Modeling
	 */
	public void resume() {
		this.paused = false;
		this.modeling.setState(ModelingState.RUNNING);
		synchronized (this.o) {
			this.o.notifyAll();
		}
	}

	@Override
	public void run() {
		while (this.running && !Thread.currentThread().isInterrupted())
			if (!this.paused) {
				try {
					Thread.sleep(ModelingTask.RUN_DELAY);
				} catch (InterruptedException e) {}
				this.modeling.run();
			} else
				try {
					while (this.paused)
						synchronized (this.o) {
							this.o.wait();
						}
				} catch (InterruptedException e) {}
		ModelingTask.LOGGER.info("Thread stopped");
	}

	public void stop() {
		this.resume();
		Thread.currentThread().interrupt();
		this.running = false;
		this.modeling.setState(ModelingState.STOPPED);
	}

}