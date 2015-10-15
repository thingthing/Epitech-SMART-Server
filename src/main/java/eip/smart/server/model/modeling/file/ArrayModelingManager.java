package eip.smart.server.model.modeling.file;

import java.util.ArrayList;
import java.util.Iterator;

import eip.smart.cscommons.model.modeling.Modeling;
import eip.smart.server.model.modeling.ModelingLogic;

/**
 * Stores the modeling it handles as an ArrayList in the memory
 */
public class ArrayModelingManager implements ModelingManager {

	private ArrayList<Modeling>	modelings	= new ArrayList<>();

	@Override
	public boolean copy(String name, String copy) {
		if (!this.exists(name))
			return (false);
		if (this.exists(copy))
			return (false);
		ModelingLogic m = new ModelingLogic(this.load(name));
		m.setName(copy);
		this.save(m);
		return (true);
	}

	@Override
	public boolean delete(String name) {
		for (Iterator<Modeling> it = this.modelings.iterator(); it.hasNext();) {
			Modeling modeling = it.next();
			if (name.equals(modeling.getName())) {
				it.remove();
				return (true);
			}
		}
		return (false);
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
	public Modeling load(String name) {
		for (Modeling modeling : this.modelings)
			if (name.equals(modeling.getName()))
				return (modeling);
		return (null);
	}

	@Override
	public void save(Modeling modeling) {
		if (!this.modelings.contains(modeling))
			this.modelings.add(modeling);
	}

}
