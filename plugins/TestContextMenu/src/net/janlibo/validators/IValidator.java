package net.janlibo.validators;

import org.eclipse.core.runtime.IStatus;

public interface IValidator {

	public IStatus validate(final String value);

}
