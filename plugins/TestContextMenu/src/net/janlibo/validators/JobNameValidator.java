package net.janlibo.validators;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class JobNameValidator implements IValidator {

	private final static JobNameValidator self = new JobNameValidator();

	public static JobNameValidator getInstance() {
		return self;
	}

	private JobNameValidator() {
		// empty
	}

	@Override
	public IStatus validate(String value) {
		if (!EmptyFieldValidator.getInstance().validate(value)) {
			return ValidationStatus.error("Job name cannot be empty.");
		}
		return ValidationStatus.ok();
	}

}
