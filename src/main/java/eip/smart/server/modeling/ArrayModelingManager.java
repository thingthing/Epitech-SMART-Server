package eip.smart.server.modeling;

import java.util.ArrayList;
import java.util.Iterator;

import eip.smart.model.Modeling;
import eip.smart.model.proxy.SimpleModelingProxy;

public class ArrayModelingManager implements ModelingManager {

	private ArrayList<Modeling>	modelings	= new ArrayList<>();

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
	public ArrayList<SimpleModelingProxy> list() {
		ArrayList<SimpleModelingProxy> modelings = new ArrayList<>();
		for (Modeling modeling : this.modelings)
			modelings.add(new SimpleModelingProxy(modeling));
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
