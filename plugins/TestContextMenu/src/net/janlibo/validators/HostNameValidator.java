package net.janlibo.validators;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class HostNameValidator implements IValidator {

	private final static HostNameValidator self = new HostNameValidator();

	public static HostNameValidator getInstance() {
		return self;
	}

	private HostNameValidator() {
		// empty
	}

	@Override
	public IStatus validate(String value) {
		if (!EmptyFieldValidator.getInstance().validate(value)) {
			return ValidationStatus.error("Host cannot be empty.");
		}
		return ValidationStatus.ok();
	}

}
