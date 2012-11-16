package org.jboss.tools.teiid.reddeer.wizard;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;

/**
 * 
 * @author apodhrad
 *
 */
public class ImportFlatSourceSelectionPage extends TeiidWizardPage {

	public static final String PAGE_TITLE = "Data File Source Selection";

	public void setDataFileSource(String dataFileSource) {
		new DefaultCombo("Data File Source", 0).setSelection(dataFileSource);
	}

	public void setCheckedFile(String fileName) {
		setCheckedFile(fileName, true);
	}

	public void setCheckedFile(String fileName, boolean checked) {
		SWTBotTable table = Bot.get().tableInGroup("Available Data Files");
		SWTBotTableItem item = table.getTableItem(fileName);
		if (checked) {
			item.check();
		} else {
			item.uncheck();
		}
	}

	public void setSourceName(String sourceName) {
		new LabeledText("Name:").setText(sourceName);
	}
}
