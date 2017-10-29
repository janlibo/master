package net.janlibo.validators;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class ArtifactNameValidator implements IValidator {

	private final static ArtifactNameValidator self = new ArtifactNameValidator();

	public static ArtifactNameValidator getInstance() {
		return self;
	}

	private ArtifactNameValidator() {
		// empty
	}

	@Override
	public IStatus validate(String value) {
		if (!EmptyFieldValidator.getInstance().validate(value)) {
			return ValidationStatus.error("Artifact name cannot be empty.");
		}
		return ValidationStatus.ok();
	}

}
