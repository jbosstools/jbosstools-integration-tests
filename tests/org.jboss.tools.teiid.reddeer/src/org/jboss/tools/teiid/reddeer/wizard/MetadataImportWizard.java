package org.jboss.tools.teiid.reddeer.wizard;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.util.Bot;

/**
 * Wizard for importing relational model from designer text file
 * 
 * @author apodhrad
 * 
 */
public class MetadataImportWizard extends TeiidImportWizard {

	public static final String DIALOG_TITLE = "Import Metadata From Text File";
	public static final String IMPORT_TYPE = "Import Type:";
	public static final String MODEL_NAME = "New Model Name: ";
	public static final String SOURCE_LOCATION = "Select Source Text File";
	public static final String TARGET_LOCATION = "Target Location";

	private String importType;
	private String source;
	private String target;

	public MetadataImportWizard() {
		super("Designer Text File >> Source or View Models");
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void execute() {
		open();
		new DefaultCombo(IMPORT_TYPE).setSelection(importType);

		next();

		// Workaround due to TEIIDDES-417
		addSelection(SOURCE_LOCATION, source);

		new DefaultCombo(SOURCE_LOCATION).setSelection(source);
		new PushButton(1).click();
		new DefaultTree().getItems().get(0).select();
		new LabeledText(MODEL_NAME).setText(target);
		new PushButton("OK").click();
		// Insure that this dialog is activated
		new DefaultShell(DIALOG_TITLE);

		finish();
	}

	// Workaround due to TEIIDDES-417
	private void addSelection(String label, final String selection) {
		SWTBotCombo botCombo = Bot.get().comboBoxWithLabel(SOURCE_LOCATION);
		final Combo combo = botCombo.widget;
		syncExec(new VoidResult() {

			@Override
			public void run() {
				combo.add(selection);
			}
		});

	}

	public class ImportType {

		public static final String RELATIONAL_MODEL = "Relational Model (XML Format)";
		public static final String RELATIONAL_TABLE = "Relational Tables (CSV Format)";
		public static final String RELATIONAL_VIRTUAL_TABLE = "Relational Virtual Tables (CSV Format)";
		public static final String RELATIONAL_DATATYPE = "Datatypes (CSV Format)";
	}

}
