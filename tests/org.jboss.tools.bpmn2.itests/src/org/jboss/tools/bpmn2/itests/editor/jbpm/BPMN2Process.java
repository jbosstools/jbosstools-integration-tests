package org.jboss.tools.bpmn2.itests.editor.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.Process;

/**
 * 
 * @author mbaluch
 */
public class BPMN2Process extends Process {

	/**
	 * 
	 * @param name
	 */
	public BPMN2Process(String name) {
		super(name);
		select();
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		properties.selectTab("Process");
		new LabeledText("Id").setText(id);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setProcessName(String name) {
		properties.selectTab("Process");
		new LabeledText("Name").setText(name);
	}
	
	/**
	 * 
	 * @param version
	 */
	public void setVersion(String version) {
		properties.selectTab("Version");
		new LabeledText("Id").setText(version);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setPackageName(String name) {
		properties.selectTab("Process");
		new LabeledText("Package Name").setText(name);
	}
	
	/**
	 * 
	 * @param b
	 */
	public void setAddHoc(boolean b) {
		properties.selectTab("Process");
//		properties.selectCheckBox(new CheckBox("Ad Hoc"), b);
		properties.selectCheckBox(new CheckBox(0), b);
	}
	
	/**
	 * 
	 */
	public void setExecutable(boolean b) {
		super.setExecutable(b);
	}
	
	/**
	 * ISSUES:
	 * 	1) Does nothing. The name is set but not visible in the file.
	 */
	public void setNameAttribute(String name) {
		properties.selectTab("Definitions");
		new LabeledText("Name").setText(name);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setTargetNamespaceAttribute(String name) {
		properties.selectTab("Definitions");
		new LabeledText("Target Namespace").setText(name);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setTypeLanguageAttribute(String name) {
		properties.selectTab("Definitions");
		new LabeledText("Type Language").setText(name);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setExpressionLanguageAttribute(String name) {
		properties.selectTab("Definitions");
		new LabeledText("Expression Language").setText(name);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void addDataType(String name) {
		properties.selectTab("Definitions");
		properties.toolbarButton("Data Type List", "Add").click();
		bot.textWithLabel("Structure").setText(name);
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void addImport(String dataType) {
		properties.selectTab("Definitions");
		properties.toolbarButton("Imports", "Add").click();
		
		String dataTypeLabel = dataType.substring(dataType.lastIndexOf(".") + 1) + " - " + dataType;
		/*
		 * Must by typed for the listener to get activated.
		 */
		SWTBot windowBot = bot.shell("Browse for a Java type to Import").bot();
		SWTBotText text = windowBot.textWithLabel("Type:");
		text.setText(dataType);
		text.typeText(" ");
		windowBot.tree().select(dataTypeLabel);
		
		new PushButton("OK").click();
	}
	
	public void addMessage(String name, String dataType) {
		properties.selectTab("Definitions");
		
		bot.toolbarButtonWithTooltip("Add", 2).click();
		bot.textWithLabel("Name").setText(name);
		
		new DataType(dataType).add();

		maximizeAndRestorePropertiesView();
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void addError(String name, String code, DataType dataType) {
		properties.selectTab("Definitions");
		
		bot.toolbarButtonWithTooltip("Add", 3).click();
		if (name != null && !name.isEmpty())
			bot.textWithLabel("Name").setText(name);
		if (code != null && !code.isEmpty())
			bot.textWithLabel("Error Code").setText(code);
		
		if (dataType != null) {
			dataType.add();
		}
		
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void addEscalation(String name, String code) {
		properties.selectTab("Definitions");
		
		bot.toolbarButtonWithTooltip("Add", 4).click();
		bot.textWithLabel("Escalation Code").setText(name);
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void addSignal(String name) {
		properties.selectTab("Definitions");
		
		bot.toolbarButtonWithTooltip("Add", 5).click();
		bot.textWithLabel("Name").setText(name);
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void addGlobalVariable(String name, String dataType) {
		properties.selectTab("Data Items");
		
		bot.toolbarButtonWithTooltip("Add", 0).click();
		bot.textWithLabel("Name").setText(name);
		
		new DataType(dataType).add();
		
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void addLocalVariable(String name, String dataType) {
		properties.selectTab("Data Items");
		
//		// TBD: the name is not the process name but file name
//		properties.toolbarButton("Variable List for Process \"" + name + "\"", "Add").click();
		
		bot.toolbarButtonWithTooltip("Add", 1).click();
		bot.textWithLabel("Name").setText(name);

		new DataType(dataType).add();
		
		maximizeAndRestorePropertiesView();
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	public void addInterface() {
		throw new UnsupportedOperationException();
	}

	/*
	 * Bug in BPMN2 Editor where the close button can be found only after it was made visible. Maximize
	 * and then restore the window to display the button.
	 */
	private void maximizeAndRestorePropertiesView() {
		for (int i=0; i<2; i++) {
			properties.maximize();
		}
	}
}
