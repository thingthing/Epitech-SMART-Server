package eip.smart.server.modeling;

import java.util.ArrayList;

import eip.smart.model.Modeling;
import eip.smart.model.proxy.SimpleModelingProxy;

public interface ModelingManager {

	public boolean delete(String name);

	public boolean exists(String name);

	public ArrayList<SimpleModelingProxy> list();

	public Modeling load(String name);

	public void save(Modeling modeling);
}
