package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class ImportFlatSourceParserPage extends TeiidWizardPage {

	public static final String PAGE_TITLE = "Flat File Delimited Columns Parser Settings";

	public static final String LABEL_FORMAT_OPRIONS = "Format Options";

	public void setColumnNamesInHeader(Boolean checked) {
		new CheckBox(LABEL_FORMAT_OPRIONS, "Column names in header").toggle(checked);
	}

	public void setHeaderLine(Integer line) {
		new LabeledText("Header line #").setText(line.toString());
	}

	public void setDataLine(Integer line) {
		new LabeledText("Data line #").setText(line.toString());
	}
}
