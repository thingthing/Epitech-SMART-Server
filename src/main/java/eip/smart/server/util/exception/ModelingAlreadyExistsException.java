package eip.smart.server.util.exception;

public class ModelingAlreadyExistsException extends Exception {
	
	private String name;
	
	public String getName() {
		return this.name;
	}

	public ModelingAlreadyExistsException(String name) {
		this.name = name;
	}

}
