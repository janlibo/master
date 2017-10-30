package net.janlibo.commands;

import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.egit.ui.internal.repository.tree.RefNode;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class BuildBranchCmd extends JenkinsBuildCmd {

	@SuppressWarnings("restriction")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		Object selectedItem = ((StructuredSelection) selection).getFirstElement();
		if (selectedItem instanceof RefNode) {
			RefNode node = (RefNode) selectedItem;
//			System.out.println("Branch: " + node.getObject().getName() + "; Commit ID: " + node.getObject().getObjectId().getName());
//			try {
//				System.out.println(node.getRepository().getBranch());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			 executeBuild(node.getObject().getName(), node.getObject().getObjectId().getName());
		}

		return null;
	}

}
