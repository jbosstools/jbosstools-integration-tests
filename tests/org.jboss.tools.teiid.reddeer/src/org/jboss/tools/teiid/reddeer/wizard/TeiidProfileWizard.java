package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 * 
 */
public abstract class TeiidProfileWizard extends NewWizardDialog {

	public static final String TITLE = "New Connection Profile";
	public static final String LABEL_NAME = "Name:";
	public static final String LABEL_DESCRIPTION = "Description (optional):";

	private String profile;
	private String name;
	private String description;

	public TeiidProfileWizard(String profile) {
		super("Connection Profiles", "Connection Profile");
		this.profile = profile;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public WizardPage getFirstPage() {
		throw new UnsupportedOperationException(getClass() + " doesn't support getFirstPage()");
	}

	@Override
	public void open() {
		log.info("Open Connection Profile");
		super.open();

		new DefaultTable().select(profile);
		new LabeledText(LABEL_NAME).setText(name);
		new LabeledText(LABEL_DESCRIPTION).setText(description);

		next();
	}

	abstract public void execute();
}
