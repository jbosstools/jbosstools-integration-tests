package org.jboss.tools.teiid.reddeer.wizard;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

/**
 * Wizard for importing a flat file source into a model project
 * 
 * @author apodhrad
 * 
 */
public class FlatImportWizard extends TeiidImportWizard {

	public static final String LABEL_FORMAT_OPRIONS = "Format Options";

	private String profile;
	private String file;
	private String name;

	private int headerLine;
	private int dataLine;

	public FlatImportWizard() {
		super("File Source (Flat) >> Source and View Model");
		// default settings
		headerLine = 1;
		dataLine = 2;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHeaderLine(int headerLine) {
		this.headerLine = headerLine;
	}

	public void setDataLine(int dataLine) {
		this.dataLine = dataLine;
	}

	@Override
	public void execute() {
		open();
		new DefaultCombo(0).setSelection(profile);
		setCheckedFile(file, true);
		// TODO: LabeledText
		new SWTWorkbenchBot().textWithLabel("Name:").setText(name + "Source");

		next();
		next();
		if (headerLine > 0) {
			new CheckBox("Column names in header").toggle(true);
			new LabeledText("Header line #").setText(Integer.toString(headerLine));
			new LabeledText("Data line #").setText(Integer.toString(dataLine));
		} else {
			new CheckBox("Column names in header").toggle(false);
		}

		next();
		// TODO: LabeledText
		new SWTWorkbenchBot().textWithLabel("Name:").setText(name + "View");

		new LabeledText("New view table name:").setText(name + "Table");

		finish();
	}

	protected void setCheckedFile(String fileName, boolean checked) {
		SWTBotTable table = new SWTWorkbenchBot().tableInGroup("Available Data Files");
		SWTBotTableItem item = table.getTableItem(fileName);
		if (checked) {
			item.check();
		} else {
			item.uncheck();
		}
	}

}
