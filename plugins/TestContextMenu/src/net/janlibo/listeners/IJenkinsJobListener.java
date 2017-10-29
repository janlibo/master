package net.janlibo.listeners;

import org.eclipse.core.runtime.IStatus;

public interface IJenkinsJobListener {

	public void handle(IStatus status);

}
