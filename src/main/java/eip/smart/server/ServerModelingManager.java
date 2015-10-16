package eip.smart.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.exception.ModelingAlreadyExistsException;
import eip.smart.server.exception.ModelingNotFoundException;
import eip.smart.server.exception.ModelingObsoleteException;
import eip.smart.server.model.modeling.ModelingLogic;
import eip.smart.server.model.modeling.ModelingTask;
import eip.smart.server.model.modeling.file.JavaFileModelingSaver;
import eip.smart.server.model.modeling.file.ModelingSaver;

public class ServerModelingManager {
	/**
	 * The current selected modeling.
	 */
	private ModelingLogic	currentModeling	= null;

	/**
	 * The current thread task the threadPool is running.
	 */
	private ModelingTask	currentTask		= null;

	/**
	 * The Manager to manage Modelings and store it.
	 * Different implementations allow to different ways of storage.
	 */
	private ModelingSaver	modelingSaver	= new JavaFileModelingSaver();

	/**
	 * Is the current modeling running.
	 */
	private boolean			running			= false;

	/**
	 * The threadPool allowing to run a Modeling.
	 */
	private ExecutorService	threadPool		= Executors.newSingleThreadExecutor();

	public ModelingLogic getCurrentModeling() {
		return this.currentModeling;
	}

	public ModelingSaver getModelingSaver() {
		return this.modelingSaver;
	}

	/**
	 * Return true if the current modeling is paused.
	 *
	 * @return
	 */
	public boolean isPaused() {
		return (this.running && this.currentTask.isPaused());
	}

	/**
	 * Return true if the current modeling is running.
	 *
	 * @return
	 */
	public boolean isRunning() {
		return (this.running);
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
		this.currentModeling = new ModelingLogic(name);
		this.modelingSaver.save(this.currentModeling);
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
		Modeling modeling = this.modelingSaver.load(name);
		this.currentModeling = new ModelingLogic(modeling);
	}

	/**
	 * Pause the current modeling.
	 */
	public void modelingPause() {
		this.currentTask.pause();
	}

	/**
	 * Resume the current modeling.
	 */
	public void modelingResume() {
		this.currentTask.resume();
	}

	/**
	 * Start the current modeling.
	 */
	public void modelingStart() {
		this.running = true;
		this.currentTask = new ModelingTask(this.currentModeling);
		this.threadPool.execute(this.currentTask);
	}

	/**
	 * Stop and save the current modeling.
	 * The modeling is then no longer the current modeling.
	 */
	public void modelingStop() {
		if (this.running) {
			this.currentTask.stop();
			this.running = false;
			this.currentTask = null;
		}
		this.modelingSaver.save(this.currentModeling);
		this.currentModeling = null;
	}

	public void stop() {
		this.threadPool.shutdown();
		this.threadPool.shutdownNow();
	}
}