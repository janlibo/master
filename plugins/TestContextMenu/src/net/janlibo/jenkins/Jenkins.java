package net.janlibo.jenkins;

import org.eclipse.jface.preference.IPreferenceStore;

import net.janlibo.Activator;
import net.janlibo.IPrefConstants;

public class Jenkins {

	private final static Jenkins self = new Jenkins();

	private final String host;
	private final String port;
	private final String username;
	private final String password;

	private Jenkins() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		this.host = store.getString(IPrefConstants.JENKINS_HOST);
		this.port = store.getString(IPrefConstants.JENKINS_PORT);
		this.username = store.getString(IPrefConstants.JENKINS_USERNAME);
		this.password = store.getString(IPrefConstants.JENKINS_PASSWORD);
	}

	public static Jenkins getInstance() {
		return self;
	}

	public String getUrl() {
		return "http://" + host + ":" + port;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public boolean useAuthorization() {
		return !getUsername().isEmpty() || !getPassword().isEmpty();
	}

}
