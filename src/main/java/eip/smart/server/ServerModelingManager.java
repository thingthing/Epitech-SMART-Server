package eip.smart.server;

import eip.smart.cscommons.configuration.Configuration;
import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.cscommons.model.modeling.ModelingState;
import eip.smart.server.model.modeling.ModelingLogic;
import eip.smart.server.model.modeling.file.ModelingSaver;
import eip.smart.server.model.modeling.file.OptimizedFileModelingSaver;
import eip.smart.server.util.exception.ModelingAlreadyExistsException;
import eip.smart.server.util.exception.ModelingNotFoundException;
import eip.smart.server.util.exception.ModelingObsoleteException;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerModelingManager {

	private final static Logger					LOGGER				= LoggerFactory.getLogger(ServerModelingManager.class);

	private HashMap<ModelingLogic, Future<?>>	currentModelings	= new HashMap<>();

	/**
	 * The threadPool allowing to run a Modeling.
	 */
	private ScheduledExecutorService			executorService		= Executors.newSingleThreadScheduledExecutor(new BasicThreadFactory.Builder().namingPattern("modelings-pool-thread-%d").build());

	/**
	 * The Manager to manage Modelings and store it.
	 * Different implementations allow to different ways of storage.
	 */
	private ModelingSaver						modelingSaver		= new OptimizedFileModelingSaver();

	public ModelingLogic getCurrentModeling() {
		Iterator<ModelingLogic> it = this.currentModelings.keySet().iterator();
		if (it.hasNext())
			return (it.next());
		return (null);
	}

	public HashMap<ModelingLogic, Future<?>> getCurrentModelings() {
		return (this.currentModelings);
	}

	public List<Modeling> getModelings() {
		List<Modeling> modelings = this.getModelingSaver().list();
		for (int i = 0; i < modelings.size(); i++)
			if (modelings.get(i).equals(this.getCurrentModeling()))
				modelings.set(i, this.getCurrentModeling());
		return (modelings);
	}

	public ModelingSaver getModelingSaver() {
		return (this.modelingSaver);
	}

	/**
	 * Return true if the current modeling is paused.
	 *
	 * @return
	 */
	public boolean isPaused() {
		return (this.isRunning() && false);// this.currentTask.isPaused());
	}

	/**
	 * Return true if the current modeling is running.
	 *
	 * @return
	 */
	public boolean isRunning() {
		return (this.currentModelings.get(this.getCurrentModeling()) != null);
	}

	/**
	 * Crate a new modeling with a given name.
	 *
	 * @param name
	 *            the name of the modeling to create.
	 * @return true if successful, false otherwise (modeling already exist).
	 * @throws ModelingAlreadyExistsException
	 */
	public void modelingCreate(String name) throws ModelingAlreadyExistsException {
		if (this.modelingSaver.exists(name))
			throw new ModelingAlreadyExistsException(name);
		this.modelingSaver.save(new ModelingLogic(name));
	}

	/**
	 * Load a given modeling and make it current.
	 *
	 * @param name
	 *            the name of the modeling to load.
	 * @return true if successful, false otherwise (another modeling is already the current modeling).
	 * @throws ModelingObsoleteException
	 */
	public void modelingLoad(String name) throws ModelingNotFoundException, ModelingObsoleteException {
		this.currentModelings.put(new ModelingLogic(this.modelingSaver.load(name)), null);
		this.getCurrentModeling().setState(ModelingState.LOADED);
	}

	/**
	 * Pause the current modeling.
	 */
	public void modelingPause() {
		// this.currentTask.pause();
	}

	/**
	 * Resume the current modeling.
	 */
	public void modelingResume() {
		// this.currentTask.resume();
	}

	public void modelingSave() {
		this.getCurrentModeling().setLastSave(new Date());
		this.getCurrentModeling().setModified(false);
		this.getModelingSaver().save(this.getCurrentModeling());
	}

	/**
	 * Start the current modeling.
	 */
	public void modelingStart() {
		int maxTick = new Configuration("server").getPropertyInteger("DEV_MAX_TICK");
		ServerModelingManager.LOGGER.debug("Developpement mode : Modeling will stop after {} ticks.", maxTick);
		this.currentModelings.put(this.getCurrentModeling(), this.executorService.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				ModelingLogic modeling = ServerModelingManager.this.getCurrentModeling();
				modeling.run();
				if (new Configuration("server").getProperty("MODE").equals("DEVELOPPEMENT") && modeling.getTick() == maxTick) {
					ServerModelingManager.LOGGER.debug("Developpement mode : Modeling stopped.");
					ServerModelingManager.this.modelingStop();
				}
			}
		}, 0, new Configuration("server").getPropertyInteger("LOOP_DELAY"), TimeUnit.MILLISECONDS));
		this.getCurrentModeling().setState(ModelingState.RUNNING);
	}

	/**
	 * Stop and save the current modeling.
	 * The modeling is then no longer the current modeling.
	 */
	public void modelingStop() {
		if (this.isRunning()) {
			this.currentModelings.get(this.getCurrentModeling()).cancel(true);
			this.getCurrentModeling().stop();
			this.getCurrentModeling().setState(ModelingState.LOADED);
			this.currentModelings.put(this.getCurrentModeling(), null);
		}
	}

	public void modelingUnload() {
		this.getCurrentModeling().setState(ModelingState.UNLOADED);
		this.currentModelings.remove(this.getCurrentModeling());
	}

	public void stop() {
		this.executorService.shutdown();
		this.executorService.shutdownNow();
	}
}