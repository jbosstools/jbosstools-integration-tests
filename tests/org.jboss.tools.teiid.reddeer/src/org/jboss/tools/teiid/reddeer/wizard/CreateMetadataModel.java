package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;

/**
 * Creates a new metadata model.
 * 
 * @author Lucia Jelinkova
 * 
 */
public class CreateMetadataModel extends NewWizardDialog {

	public static class ModelClass {

		public static final String RELATIONAL = "Relational";

		public static final String XML = "XML";

		public static final String XSD = "XML Schema (XSD)";

		public static final String WEBSERVICE = "Web Service";

		public static final String MODEL_EXTENSION = "Model Extension (Deprecated)";

		public static final String FUNCTION = "Function (Deprecated)";

	}

	public static class ModelType {

		public static final String SOURCE = "Source Model";

		public static final String VIEW = "View Model";

		public static final String DATATYPE = "Datatype Model";

		public static final String EXTENSION = "Model Class Extension";

		public static final String FUNCTION = "User Defined Function";

	}

	private String location;

	private String name;

	private String clazz;

	private String type;

	public CreateMetadataModel() {
		super("Teiid Designer", "Teiid Metadata Model");
	}

	public void execute() {
		open();
		fillFirstPage();
		finish();

		xsdSchemaSelection();
	}

	private void fillFirstPage() {
		new LabeledText("Location:").setText(location);
		new LabeledText("Model Name:").setText(name);
		new DefaultCombo("Model Class:").setSelection(clazz);
		new DefaultCombo("Model Type:").setSelection(type);
	}

	private void xsdSchemaSelection() {
		if (ModelClass.XSD.equals(clazz)) {
			new DefaultShell("Model Initializer");
			Bot.get().table().select("XML Schema (2001)");
			new PushButton("OK").click();
		}
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setClass(String clazz) {
		this.clazz = clazz;
	}
}
