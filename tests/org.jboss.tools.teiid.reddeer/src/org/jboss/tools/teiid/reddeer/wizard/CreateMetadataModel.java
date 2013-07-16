package org.jboss.tools.teiid.reddeer.wizard;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.hamcrest.Matcher;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
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
	
	public static class ModelBuilder {
		
		public static final String TRANSFORM_EXISTING = "Transform from an existing model";
		
		public static final String COPY_EXISTING = "Copy from an existing model of the same model class";
		
		public static final String BUILD_FROM_XML_SCHEMA = "Build XML documents from XML schema";
	}

	private String location;

	private String name;

	private String clazz;

	private String type;
	
	private String modelBuilder = "";


	public CreateMetadataModel() {
		super("Teiid Designer", "Teiid Metadata Model");
	}

	public void execute() {
		open();
		fillFirstPage();
		finish();

		xsdSchemaSelection();
	}
	
	public void execute(String modelBuilderType, String... pathToExistingModel){
		open();
		fillFirstPage(modelBuilderType);
		fillSecondPage(modelBuilderType, pathToExistingModel);
		finish();
	}
	
	public void execute(String[] pathToXmlSchema, String rootElement){
		open();
		fillFirstPage();
		if (modelBuilder.equals(ModelBuilder.BUILD_FROM_XML_SCHEMA)){
			new DefaultTable().select(modelBuilder);
			next();
			fillSecondPage(pathToXmlSchema, rootElement);
		} else {
			throw new UnsupportedOperationException();
		}
		finish();
	}

	private void fillFirstPage() {
		new LabeledText("Location:").setText(location);
		new LabeledText("Model Name:").setText(name);
		new DefaultCombo("Model Class:").setSelection(clazz);
		new DefaultCombo("Model Type:").setSelection(type);
	}
	
	private void fillFirstPage(String modelBuilderType) {
		new LabeledText("Location:").setText(location);
		new LabeledText("Model Name:").setText(name);
		new DefaultCombo("Model Class:").setSelection(clazz);
		new DefaultCombo("Model Type:").setSelection(type);
		new DefaultTable().select(modelBuilderType);//ModelBuilder.TRANSFORM_EXISTING
		new PushButton("&Next >").click();
	}
	
	private void fillSecondPage(String modelBuilderType, String... pathToExistingModel){
		new PushButton("...").click();
		new DefaultTreeItem(pathToExistingModel).select();
		new PushButton("OK").click();
	}
	
	private void fillSecondPage(String[] pathToXmlSchema, String rootElement){
		new PushButton(0).click();
		new DefaultTreeItem(pathToXmlSchema).select();
		new PushButton("OK").click();
		new DefaultTable().select(rootElement);
		new PushButton(1).click();// >
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
	
	public void setModelBuilder(String modelBuilder) {
		this.modelBuilder = modelBuilder;
	}
}
