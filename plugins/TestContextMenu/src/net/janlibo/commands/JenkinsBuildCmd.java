package net.janlibo.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import net.janlibo.dialogs.RunJobDialog;

abstract class JenkinsBuildCmd extends AbstractHandler {

	protected void executeBuild(final String branchName, final String commitName) {
		System.out.println("Branch: " + polishBranchName(branchName));
		System.out.println("Commit: " + commitName);
		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		if (shell != null) {			
			new RunJobDialog(shell, polishBranchName(branchName), commitName).open();
		}
	}
	
	private String polishBranchName(final String branchName) {
		if (branchName.startsWith(Constants.R_HEADS)) {
			return branchName.replaceFirst(Constants.R_HEADS, "");
		}
			
		return branchName;
	}

}
