package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.swt.impl.text.LabeledText;

public class ImportFlatSourceViewPage extends TeiidWizardPage {

	public static final String PAGE_TITLE = "View Model Definition";

	public void setViewName(String viewName) {
		new LabeledText("Name:").setText(viewName);
	}

	public void setTableName(String tableName) {
		new LabeledText("New view table name:").setText(tableName);
	}
}
