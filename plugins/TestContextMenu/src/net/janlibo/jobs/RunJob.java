package net.janlibo.jobs;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import net.janlibo.jenkins.Jenkins;
import net.janlibo.listeners.IJenkinsJobListener;

public class RunJob implements Runnable {

	IJenkinsJobListener executionFinishedListener;
	String branchName;
	String commitName;
	Jenkins jenkins = Jenkins.getInstance();
	List<String> jobNames;

	public RunJob(final List<String> jobNames, final String branchName, final String commitName,
			final IJenkinsJobListener listener) {
		this.executionFinishedListener = listener;
		this.branchName = branchName;
		this.commitName = commitName;
		this.jobNames = jobNames;
	}

	@Override
	public void run() {
		Client client = null;
		try {

			client = createClient();

			for (final String jobName : jobNames) {
				processRequest(client, jobName);
			}

		} catch (Exception ex) {
			executionFinishedListener.handle(ValidationStatus.error(ex.getMessage()));
		} finally {
			if (client != null) {
				client.destroy();
			}
		}

		executionFinishedListener.handle(ValidationStatus.ok());
	}

	private Client createClient() {
		Client client = Client.create();
		if (jenkins.useAuthorization()) {
			client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(jenkins.getUsername(),
					jenkins.getPassword()));
		}
		return client;
	}

	private void processRequest(final Client client, final String jobName) {

		WebResource webResource = client.resource(jenkins.getUrl() + "/job/" + jobName + "/buildWithParameters");

		Builder builder = webResource.header("Content-type", MediaType.APPLICATION_FORM_URLENCODED + ";charset=UTF-8");
		final String jenkinsCrumb = getCrumb(client);

		if (jenkinsCrumb != null) {
			builder.header("Jenkins-Crumb", jenkinsCrumb);
		}

		String input = "branch=" + branchName + "&commit=" + commitName + "&delay=0sec&token=simpletoken";
		URI uri = UriBuilder.fromPath(input).build();
		ClientResponse response = builder.post(ClientResponse.class, uri.toString());

		String jsonResponse = response.getEntity(String.class);

		System.out.println("Response:" + jsonResponse);
	}

	private String getCrumb(final Client client) {
		try {
			WebResource webResource = client.resource(jenkins.getUrl() + "/crumbIssuer/api/json");
			ClientResponse response = webResource.get(ClientResponse.class);
			String crumbJson = response.getEntity(String.class);
			JSONObject jsonResponse = new JSONObject(crumbJson);
			return jsonResponse.getString("crumb");
		} catch (Exception ex) {
			return null;
		}
	}

}
