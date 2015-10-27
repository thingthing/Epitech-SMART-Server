package eip.smart.server.model.modeling.file;

import java.util.ArrayList;
import java.util.Iterator;

import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.model.modeling.ModelingLogic;
import eip.smart.server.util.exception.ModelingAlreadyExistsException;
import eip.smart.server.util.exception.ModelingNotFoundException;

/**
 * Stores the modeling it handles as an ArrayList in the memory
 */
public class ArrayModelingSaver implements ModelingSaver {

	private ArrayList<Modeling>	modelings	= new ArrayList<>();

	@Override
	public void clear() {
		for (Iterator<Modeling> it = this.modelings.iterator(); it.hasNext();) {
			it.next();
			it.remove();
		}
	}

	@Override
	public void copy(String name, String copy) throws ModelingNotFoundException, ModelingAlreadyExistsException {
		if (!this.exists(name))
			throw new ModelingNotFoundException(name);
		if (this.exists(copy))
			throw new ModelingAlreadyExistsException(copy);
		ModelingLogic m = new ModelingLogic(this.load(name));
		m.setName(copy);
		this.save(m);
	}

	@Override
	public void delete(String name) throws ModelingNotFoundException {
		for (Iterator<Modeling> it = this.modelings.iterator(); it.hasNext();) {
			Modeling modeling = it.next();
			if (name.equals(modeling.getName())) {
				it.remove();
				return;
			}
		}
		throw new ModelingNotFoundException(name);
	}

	@Override
	public boolean exists(String name) {
		for (Modeling modeling : this.modelings)
			if (name.equals(modeling.getName()))
				return (true);
		return (false);
	}

	@Override
	public ArrayList<Modeling> list() {
		ArrayList<Modeling> modelings = new ArrayList<>();
		for (Modeling modeling : this.modelings)
			modelings.add(new Modeling(modeling));
		return (modelings);
	}

	@Override
	public Modeling load(String name) throws ModelingNotFoundException {
		for (Modeling modeling : this.modelings)
			if (name.equals(modeling.getName()))
				return (modeling);
		throw new ModelingNotFoundException(name);
	}

	@Override
	public void save(Modeling modeling) {
		if (!this.modelings.contains(modeling))
			this.modelings.add(modeling);
	}

}
