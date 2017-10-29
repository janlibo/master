package net.janlibo.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import net.janlibo.Activator;
import net.janlibo.IPrefConstants;
import net.janlibo.jobs.RunJob;
import net.janlibo.listeners.IJenkinsJobListener;

public class RunJobDialog extends Dialog {

	String branchName;
	String commitName;
	Text artifactTF;
	ProgressBar progressBar;
	Button runBtn;
	Button winJobCheckbox;
	Button macJobCheckbox;
	String artifactName;
	IJenkinsJobListener jobListener;

	public RunJobDialog(Shell parentShell, final String branchName, final String commitName) {
		super(parentShell);
		this.branchName = branchName;
		this.commitName = commitName;
		this.jobListener = createJobListener();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Run Jenkin's Job");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		Group group = new Group(container, SWT.None);
		group.setLayout(new GridLayout(1, false));
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setText("Select target platform");

		winJobCheckbox = new Button(group, SWT.CHECK);
		winJobCheckbox.setText("Windows");
		setDefaultValue(winJobCheckbox, IPrefConstants.JOB_LAST_SELECTION_WIN);
		storeValueAfterChange(winJobCheckbox, IPrefConstants.JOB_LAST_SELECTION_WIN);

		macJobCheckbox = new Button(group, SWT.CHECK);
		macJobCheckbox.setText("macOS");
		setDefaultValue(macJobCheckbox, IPrefConstants.JOB_LAST_SELECTION_MAC);
		storeValueAfterChange(macJobCheckbox, IPrefConstants.JOB_LAST_SELECTION_MAC);

		progressBar = new ProgressBar(container, SWT.INDETERMINATE);
		progressBar.setEnabled(false);
		progressBar.setState(SWT.PAUSED);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		progressBar.setLayoutData(gd);

		return container;
	}

	private void storeValueAfterChange(final Button btn, final String prefName) {
		btn.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				getStore().setValue(prefName, btn.getSelection());
			}
		});
	}

	private void setDefaultValue(final Button btn, final String prefName) {
		btn.setSelection(getStore().getBoolean(prefName));
	}

	private IPreferenceStore getStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		runBtn = createButton(parent, 123, "Run", true);
		runBtn.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				runBtn.setEnabled(false);
				progressBar.setState(SWT.NORMAL);
				new Thread(new RunJob(getJobNames(), branchName, commitName, jobListener)).start();
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CLOSE_LABEL, false);
	}

	private List<String> getJobNames() {
		final List<String> jobNames = new ArrayList<>();
		if (winJobCheckbox.getSelection())
			jobNames.add(getStore().getString(IPrefConstants.JOB_NAME_WIN));
		if (macJobCheckbox.getSelection())
			jobNames.add(getStore().getString(IPrefConstants.JOB_NAME_MAC));
		return jobNames;
	}

	private IJenkinsJobListener createJobListener() {
		return new IJenkinsJobListener() {

			@Override
			public void handle(final IStatus status) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {
						if (status.isOK()) {
							MessageDialog.openInformation(getShell(), "Job Execution",
									"Job has been sucsessfully executed");
							okPressed();
						} else {
							MessageDialog.openError(getShell(), "Error", status.getMessage());
						}
					}
				});
			}
		};

	}

}
