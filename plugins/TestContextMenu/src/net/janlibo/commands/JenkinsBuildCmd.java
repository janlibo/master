package net.janlibo.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import net.janlibo.dialogs.RunJobDialog;

public class JenkinsBuildCmd extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	protected void executeBuild(final String branchName, final String commitName) {
		System.out.println("Branch: " + branchName);
		System.out.println("Commit: " + commitName);
		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		if (shell != null) {
			new RunJobDialog(shell, branchName, commitName).open();
		}
	}

}