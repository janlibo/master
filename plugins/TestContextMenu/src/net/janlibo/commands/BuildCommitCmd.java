package net.janlibo.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.ui.handlers.HandlerUtil;

public class BuildCommitCmd extends JenkinsBuildCmd {

	private final static String HEAD_REV_NAME = "HEAD";

	@SuppressWarnings("rawtypes")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		// GenericHistoryView historyView = (GenericHistoryView)
		// HandlerUtil.getActiveWorkbenchWindow(event)
		// .getActivePage().getActivePart();
		// GitHistoryPage gitHistoryPage = (GitHistoryPage)
		// historyView.getCurrentPage();
		// Object input = gitHistoryPage.getInput();
		// System.out.println("Input: " + input);
		Object selectedItem = ((StructuredSelection) selection).getFirstElement();
		List<String> refs = new ArrayList<>();
		if (selectedItem instanceof PlotCommit) {
			loopRefs(refs, (PlotCommit) selectedItem);
			System.out.println("Commit Id: " + ((PlotCommit) selectedItem).getId().getName());

			// for (int i = 0; i < ((PlotCommit) selectedItem).getRefCount();
			// i++) {
			// Ref ref = ((PlotCommit) selectedItem).getRef(i);
			// System.out.println("Ref(" + i + ") - Branch: " + ref.getName());
			// System.out.println("Ref(" + i + ") - Branch: " +
			// ref.getObjectId().getName());
			// }
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private void loopRefs(List<String> refs, PlotCommit item) {
		for (int i = 0; i < item.getRefCount(); i++) {
			Ref ref = item.getRef(i);
			// if (isUniqueBranch(refs, ref.getName())) {
			if (ref.getName().contains("/origin/")) {
				append(refs, ref.getName());
			}
		}
		if (item.getChildCount() > 0) {
			for (int i = 0; i < item.getChildCount(); i++) {
				loopRefs(refs, item.getChild(i));
			}
		}
	}

	private boolean isUniqueBranch(List<String> refs, String ref) {
		if (ref.equalsIgnoreCase(HEAD_REV_NAME)) {
			return false;
		}

		for (String item : refs) {
			final String itemBranchName = item.contains("/") ? item.substring(item.lastIndexOf("/")) : item;
			final String refBranchName = ref.contains("/") ? ref.substring(ref.lastIndexOf("/")) : ref;
			System.out.println("comparing " + itemBranchName + " with " + refBranchName);
			if (itemBranchName.equalsIgnoreCase(refBranchName)) {
				if (ref.length() > item.length()) {
					refs.remove(item);
					break;
				}
				return false;
			}
		}

		return true;
	}

	private void append(List<String> refs, String ref) {
		if (ref.startsWith("refs/")) {
			ref = ref.replaceFirst("refs/", "");
		}
		System.out.println("adding: " + ref);
		refs.add(ref);
	}

}
