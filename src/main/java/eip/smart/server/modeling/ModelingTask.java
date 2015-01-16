package eip.smart.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import eip.smart.model.Modeling;
import eip.smart.server.servlet.ModelingInfo;

public class ModelingTask implements Runnable {

	private final static Logger	LOGGER			= Logger.getLogger(ModelingInfo.class.getName());

	private Object				o				= new Object();
	private volatile boolean	paused			= false;
	private Modeling			currentModeling	= null;
	private boolean				running			= true;

	public ModelingTask(Modeling currentModeling) {
		this.currentModeling = currentModeling;
	}

	public boolean isPaused() {
		return this.paused;
	}

	public void pause() {
		this.paused = true;
	}

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