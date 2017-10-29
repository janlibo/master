package net.janlibo.validators;

public class EmptyFieldValidator {

	private final static EmptyFieldValidator self = new EmptyFieldValidator();

	public static EmptyFieldValidator getInstance() {
		return self;
	}

	private EmptyFieldValidator() {
		// empty
	}

	public boolean validate(String value) {
		return (value != null && !value.isEmpty());
	}

}
