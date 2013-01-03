package org.jboss.tools.teiid.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;

/**
 * Wizard for importing a flat file source into a model project
 * 
 * @author apodhrad
 *
 */
public class ImportFlatSourceWizard extends ImportWizardDialog {

	private List<TeiidWizardPage> pageList;

	public ImportFlatSourceWizard() {
		super("Teiid Designer", "File Source (Flat) >> Source and View Model");
		pageList = new ArrayList<TeiidWizardPage>();
		pageList.add(null);
		pageList.add(new ImportFlatSourceSelectionPage());
		pageList.add(new ImportFlatSourceFormatPage());
		pageList.add(new ImportFlatSourceParserPage());
		pageList.add(new ImportFlatSourceViewPage());
	}

	public TeiidWizardPage openWizard() {
		open();
		return pageList.get(currentPage);
	}

	public TeiidWizardPage nextPage() {
		next();
		return pageList.get(currentPage);
	}

	@Override
	public WizardPage getFirstPage() {
		throw new UnsupportedOperationException();
	}

}
