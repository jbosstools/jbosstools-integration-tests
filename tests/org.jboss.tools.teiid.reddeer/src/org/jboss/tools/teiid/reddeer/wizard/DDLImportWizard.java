package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard for importing relational model from DDL
 * 
 * @author apodhrad
 *
 */
public class DDLImportWizard extends TeiidImportWizard {

	public static final String DDL_FILE = "DDL file: ";
	public static final String MODEL_FOLDER = "Model folder: ";
	public static final String MODEL_NAME = "Model name: ";
	public static final String MODEL_TYPE = "Model type: ";

	private String ddlPath;
	private String modelFolder;
	private String modelName;
	private String modelType;

	public DDLImportWizard() {
		super("DDL File >> Source or View Model");
	}

	public void setDdlPath(String ddlPath) {
		this.ddlPath = ddlPath;
	}

	public void setModelFolder(String modelFolder) {
		this.modelFolder = modelFolder;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public void execute() {
		open();

		new DefaultCombo(DDL_FILE).setText(ddlPath);
		// new LabeledText(MODEL_FOLDER).setText(modelFolder);
		new LabeledText(MODEL_NAME).setText(modelName);
		// new DefaultCombo(MODEL_FOLDER).setText(modelFolder);

		next();
		finish();
	}

}
