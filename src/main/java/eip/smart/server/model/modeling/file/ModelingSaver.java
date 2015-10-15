package eip.smart.server.model.modeling.file;

import java.util.List;

import eip.smart.cscommons.model.modeling.Modeling;

/**
 * General interface for a Modeling manager, its main purpose is to store and load modelings.
 */
public interface ModelingSaver {

	public boolean copy(String name, String copy);// throws ModelingAlreadyExistsException, ModelingNotFoundException;

	/**
	 * Deletes the modeling associated with given name
	 *
	 * @param name
	 * @return false if no suck modeling exists
	 */
	public boolean delete(String name);

	/**
	 * @param name
	 * @return true if a modeling exists with the given name
	 */
	public boolean exists(String name);

	/**
	 * @return An array of the existing modelings
	 */
	public List<Modeling> list();

	/**
	 * Loads the given modeling
	 *
	 * @param name
	 * @return the fully loaded ready to use Modeling
	 */
	public Modeling load(String name);

	/**
	 * Saves the given modeling
	 *
	 * @param modeling
	 */
	public void save(Modeling modeling);
}
