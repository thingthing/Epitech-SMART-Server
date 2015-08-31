package eip.smart.server.model.modeling;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ModelingTask implements Runnable {

	private final static Logger	LOGGER			= Logger.getLogger(ModelingTask.class.getName());

	private ModelingLogic		currentModeling	= null;
	private Object				o				= new Object();
	private volatile boolean	paused			= false;
	private boolean				running			= true;

	/**
	 * Creates a new ModelingTask using given argument as current Modeling
	 *
	 * @param currentModeling
	 */
	public ModelingTask(ModelingLogic currentModeling) {
		this.currentModeling = currentModeling;
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
	}

	/**
	 * Resumes the Modeling
	 */
	public void resume() {
		this.paused = false;
		synchronized (this.o) {
			this.o.notifyAll();
		}
	}

	@Override
	public void run() {
		while (this.running && !Thread.currentThread().isInterrupted())
			if (!this.paused) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				this.currentModeling.run();
			} else
				try {
					while (this.paused)
						synchronized (this.o) {
							this.o.wait();
						}
				} catch (InterruptedException e) {}
		ModelingTask.LOGGER.log(Level.INFO, "Thread stopped");
	}

	public void stop() {
		this.resume();
		Thread.currentThread().interrupt();
		this.running = false;
	}

}