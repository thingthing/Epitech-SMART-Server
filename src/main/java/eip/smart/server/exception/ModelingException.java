package eip.smart.server.exception;

public class ModelingException extends Exception {

	private String	modelingName;

	public ModelingException(String name) {
		this.modelingName = name;
	}

	public String getModelingName() {
		return this.modelingName;
	}

}