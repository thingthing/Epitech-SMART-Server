package eip.smart.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import eip.smart.cscommons.configuration.Configuration;
import eip.smart.server.model.modeling.ModelingLogic;
import eip.smart.server.model.modeling.file.JavaFileModelingSaver;
import eip.smart.server.model.modeling.file.ModelingSaver;
import eip.smart.server.util.exception.ModelingAlreadyExistsException;
import eip.smart.server.util.exception.ModelingNotFoundException;
import eip.smart.server.util.exception.ModelingObsoleteException;

public class ServerModelingManager {

	private HashMap<ModelingLogic, Future<?>>	currentModelings	= new HashMap<>();

	/**
	 * The threadPool allowing to run a Modeling.
	 */
	private ScheduledExecutorService			executorService		= Executors.newSingleThreadScheduledExecutor();

	/**
	 * The Manager to manage Modelings and store it.
	 * Different implementations allow to different ways of storage.
	 */
	private ModelingSaver						modelingSaver		= new JavaFileModelingSaver();

	public ModelingLogic getCurrentModeling() {
		Iterator<ModelingLogic> it = this.currentModelings.keySet().iterator();
		if (it.hasNext())
			return (it.next());
		return (null);
	}

	public HashMap<ModelingLogic, Future<?>> getCurrentModelings() {
		return (this.currentModelings);
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
			throw new ModelingAlreadyExistsException();
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

	/**
	 * Start the current modeling.
	 */
	public void modelingStart() {
		this.currentModelings.put(this.getCurrentModeling(), this.executorService.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				ServerModelingManager.this.getCurrentModeling().run();
			}
		}, 0, new Configuration("server").getPropertyInteger("LOOP_DELAY"), TimeUnit.MILLISECONDS));
	}

	/**
	 * Stop and save the current modeling.
	 * The modeling is then no longer the current modeling.
	 */
	public void modelingStop() {
		if (this.isRunning()) {
			this.currentModelings.get(this.getCurrentModeling()).cancel(true);
			this.getCurrentModeling().stop();
			this.currentModelings.put(this.getCurrentModeling(), null);
		}
	}

	public void modelingUnload() {
		this.currentModelings.remove(this.getCurrentModeling());
	}

	public void stop() {
		this.executorService.shutdown();
		this.executorService.shutdownNow();
	}
}