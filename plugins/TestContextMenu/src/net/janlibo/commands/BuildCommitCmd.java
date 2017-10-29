package net.janlibo.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.egit.core.AdapterUtils;
import org.eclipse.egit.core.internal.util.ResourceUtil;
import org.eclipse.egit.ui.internal.history.GitHistoryPage;
import org.eclipse.egit.ui.internal.history.HistoryPageInput;
import org.eclipse.egit.ui.internal.repository.tree.RepositoryTreeNode;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefDatabase;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.RevWalkUtils;
import org.eclipse.team.internal.ui.history.GenericHistoryView;
import org.eclipse.ui.handlers.HandlerUtil;

public class BuildCommitCmd extends JenkinsBuildCmd {

	private final static String HEAD_REV_NAME = "HEAD";

	@SuppressWarnings("rawtypes")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		GenericHistoryView historyView = (GenericHistoryView) HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getActivePart();
		GitHistoryPage gitHistoryPage = (GitHistoryPage) historyView.getCurrentPage();
		Object input = gitHistoryPage.getInput();
		Repository repo = getRepository(input);
		List<Ref> allRefs = CommitMessageViewer_getBranches(repo);
		Object selectedItem = ((StructuredSelection) selection).getFirstElement();
		// List<String> refs = new ArrayList<>();
		if (selectedItem instanceof PlotCommit) {
			AdapterUtils.adapt(selectedItem, RevCommit.class);
			// loopRefs(refs, (PlotCommit) selectedItem);
			CommitInfoBuilder_format(repo, (PlotCommit) selectedItem, allRefs);
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

	private Repository getRepository(Object input) {
		if (input == null) {
			return null;
		}
		if (input instanceof HistoryPageInput) {
			return ((HistoryPageInput) input).getRepository();
		}
		if (input instanceof RepositoryTreeNode) {
			return ((RepositoryTreeNode) input).getRepository();
		}
		Repository repo = AdapterUtils.adapt(input, Repository.class);
		if (repo != null) {
			return repo;
		}
		IResource resource = AdapterUtils.adaptToAnyResource(input);
		if (resource != null) {
			Repository repository = ResourceUtil.getRepository(resource);
			if (repository != null) {
				return repository;
			}
		}

		return null;
	}

	private void CommitInfoBuilder_format(Repository db, RevCommit commit, Collection<Ref> allRefs) {
		try (RevWalk rw = new RevWalk(db)) {
			List<Ref> branches = CommitInfoBuilder_getBranches(commit, allRefs, db);
			if (!branches.isEmpty()) {
				for (Iterator<Ref> i = branches.iterator(); i.hasNext();) {
					Ref head = i.next();
					RevCommit p;
					p = rw.parseCommit(head.getObjectId());
					// addLink(d, formatHeadRef(head), hyperlinks, p);
					System.out.println("Ref: " + head);
					System.out.println("RevCommit: " + p);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private List<Ref> CommitInfoBuilder_getBranches(RevCommit commit, Collection<Ref> allRefs, Repository db)
			throws MissingObjectException, IncorrectObjectTypeException, IOException {
		try (RevWalk revWalk = new RevWalk(db)) {
			revWalk.setRetainBody(false);
			return RevWalkUtils.findBranchesReachableFrom(commit, revWalk, allRefs);
		}
	}

	private static List<Ref> CommitMessageViewer_getBranches(Repository repo) {
		List<Ref> ref = new ArrayList<>();
		try {
			RefDatabase refDb = repo.getRefDatabase();
			ref.addAll(refDb.getRefs(Constants.R_HEADS).values());
			ref.addAll(refDb.getRefs(Constants.R_REMOTES).values());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ref;
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
