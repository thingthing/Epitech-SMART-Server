package eip.smart.server;

import java.util.ArrayList;

import eip.smart.model.Modeling;
import eip.smart.model.proxy.SimpleModelingProxy;

public abstract class ModelingManager {

	public abstract boolean delete(String name);

	public abstract boolean exists(String name);

	public abstract ArrayList<SimpleModelingProxy> list();

	public abstract Modeling load(String name);

	public abstract void save(Modeling modeling);
}
