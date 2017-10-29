package net.janlibo.validators;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class PortValidator implements IValidator {

	final int MIN_VALUE = 10;
	final int MAX_VALUE = 65535;

	private final static PortValidator self = new PortValidator();

	public static PortValidator getInstance() {
		return self;
	}

	private PortValidator() {
		// empty
	}

	@Override
	public IStatus validate(String value) {
		if (!EmptyFieldValidator.getInstance().validate(value)) {
			return ValidationStatus.error("Port cannot be empty.");
		} else if (!NumericFieldValidator.getInstance().validate(value, MIN_VALUE, MAX_VALUE)) {
			return ValidationStatus.error("Port must be a number between " + MIN_VALUE + " and " + MAX_VALUE);
		}
		return ValidationStatus.ok();
	}

}
