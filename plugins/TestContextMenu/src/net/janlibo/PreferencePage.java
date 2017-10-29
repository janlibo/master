package net.janlibo;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import net.janlibo.validators.HostNameValidator;
import net.janlibo.validators.IValidator;
import net.janlibo.validators.JobNameValidator;
import net.janlibo.validators.PortValidator;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	StringFieldEditor hostField;
	StringFieldEditor portField;
	StringFieldEditor userField;
	StringFieldEditor pwdField;
	StringFieldEditor jobNameWinField;
	StringFieldEditor jobNameMacField;
	// StringFieldEditor buildNameField;

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Parameters for remote job execution on Jenkins:");
	}

	@Override
	protected void createFieldEditors() {
		Composite cmp = getFieldEditorParent();

		hostField = new StringFieldEditor(IPrefConstants.JENKINS_HOST, "Host:", cmp);
		portField = new StringFieldEditor(IPrefConstants.JENKINS_PORT, "Port:", cmp);
		userField = new StringFieldEditor(IPrefConstants.JENKINS_USERNAME, "Username:", cmp);
		pwdField = new StringFieldEditor(IPrefConstants.JENKINS_PASSWORD, "Password:", cmp);
		pwdField.getTextControl(cmp).setEchoChar('*');
		jobNameWinField = new StringFieldEditor(IPrefConstants.JOB_NAME_WIN, "Job Name (Windows):", cmp);
		jobNameMacField = new StringFieldEditor(IPrefConstants.JOB_NAME_MAC, "Job Name (macOS):", cmp);
		// buildNameField = new StringFieldEditor(IPrefConstants.ARTIFACT_NAME,
		// "Artifact Name:", cmp);

		addField(hostField);
		addField(portField);
		addField(userField);
		addField(pwdField);
		addField(jobNameWinField);
		addField(jobNameMacField);
		// addField(buildNameField);
	}

	@Override
	protected void checkState() {
		super.checkState();
		Map<String, IValidator> values = new HashMap<>();
		values.put(hostField.getStringValue(), HostNameValidator.getInstance());
		values.put(portField.getStringValue(), PortValidator.getInstance());
		values.put(jobNameWinField.getStringValue(), JobNameValidator.getInstance());
		values.put(jobNameMacField.getStringValue(), JobNameValidator.getInstance());
		// values.put(buildNameField.getStringValue(),
		// ArtifactNameValidator.getInstance());

		validate(values);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		if (event.getProperty().equals(FieldEditor.VALUE)) {
			checkState();
		}
	}

	private void validate(final Map<String, IValidator> fields) {
		for (final String value : fields.keySet()) {
			IValidator validator = fields.get(value);
			final IStatus validationStatus = validator.validate(value);

			setErrorMessage(validationStatus.isOK() ? null : validationStatus.getMessage());
			setValid(validationStatus.isOK());

			if (!validationStatus.isOK()) {
				return;
			}
		}
	}

}
