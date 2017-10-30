package net.janlibo;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PrefInitializer extends AbstractPreferenceInitializer {

	public PrefInitializer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(IPrefConstants.JENKINS_HOST, "10.11.40.130");
		store.setDefault(IPrefConstants.JENKINS_PORT, "8080");
		store.setDefault(IPrefConstants.JENKINS_USERNAME, "");
		store.setDefault(IPrefConstants.JENKINS_PASSWORD, "");
		store.setDefault(IPrefConstants.JOB_NAME_WIN, "TEFM_COMMIT_BUILD_WIN");
		store.setDefault(IPrefConstants.JOB_NAME_MAC, "TEFM_COMMIT_BUILD_MAC");
		store.setDefault(IPrefConstants.JOB_LAST_SELECTION_WIN, true);
		store.setDefault(IPrefConstants.JOB_LAST_SELECTION_MAC, false);
		// store.setDefault(IPrefConstants.ARTIFACT_NAME, "artifact_name");
	}

}
