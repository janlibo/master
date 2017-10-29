package net.janlibo.validators;

public class NumericFieldValidator {

	private final static NumericFieldValidator self = new NumericFieldValidator();

	public static NumericFieldValidator getInstance() {
		return self;
	}

	private NumericFieldValidator() {
		// empty
	}

	public boolean validate(String textValue, int min, int max) {
		try {
			long value = Long.parseLong(textValue);
			if (value >= min && value <= max) {
				return true;
			}
		} catch (NumberFormatException ex) {
			return false;
		}
		return false;
	}
}
