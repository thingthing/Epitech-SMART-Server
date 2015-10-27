package eip.smart.server.util.exception;

public class ModelingNotFoundException extends Exception {

	private String name;
	
	public String getName() {
		return this.name;
	}
	
	public ModelingNotFoundException(String name) {
		this.name = name;
	}

}
