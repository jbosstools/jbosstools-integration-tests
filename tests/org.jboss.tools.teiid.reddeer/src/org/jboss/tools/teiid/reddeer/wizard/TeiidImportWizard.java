package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;

/**
 * Abstract wizard for importing relational models from various sources
 * 
 * @author apodhrad
 * 
 */
public abstract class TeiidImportWizard extends ImportWizardDialog {

	private String importer;

	public TeiidImportWizard(String importer) {
		super("Teiid Designer", importer);
		this.importer = importer;
	}

	@Override
	public WizardPage getFirstPage() {
		throw new UnsupportedOperationException("TeiidImportWizard doesn't support getFirstPage()");
	}

	@Override
	public void open() {
		log.info("Open " + importer);
		super.open();
	}

	@Override
	public void finish() {
		stupidWait();
		super.finish();
		// wait for 'Progress Information'
		log.info("Progress waiting started ...");
		new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
		log.info("Progress waiting stopped.");
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		log.info("No running job");
		try {
			new ShellMenu("File", "Save All").select();
			log.info("All files saved.");
		} catch (Exception e) {
			log.info("There is nothing to save.");
		}
	}

	// The are some problems on Win7_32. We need to wait due to possible WSDL
	// reading (not sure).
	private void stupidWait() {
		long time = 10 * 1000;
		log.info("Stupid waiting for " + time + " ms");
		Bot.get().sleep(time);
	}

	public abstract void execute();

}
