package org.jboss.tools.cdi.reddeer.cdi.ui;

import java.util.List;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.WaitUntil;

public class NewDecoratorCreationWizard extends NewWizardDialog{
	
	public static final String CATEGORY="CDI (Context and Dependency Injection)";
	public static final String NAME="Decorator";
	
	public NewDecoratorCreationWizard(){
		super(CATEGORY,NAME);
	}
	
	public void setPackage(String packageName){
		new LabeledText("Package:").setText(packageName);
	}
	
	public void setName(String name){
		new LabeledText("Name:").setText(name);
	}
	
	public void setPublic(boolean isPublic){
		new RadioButton("public").toggle(isPublic);
	}
	
	public void setDefault(boolean isDefault){
		new RadioButton("default").toggle(isDefault);
	}
	
	public void setPrivate(boolean isPrivate){
		new RadioButton("private").toggle(isPrivate);
	}

	public void setProtected(boolean isProtected){
		new RadioButton("protected").toggle(isProtected);
	}
	
	public void setFinal(boolean isFinal){
		new CheckBox("final").toggle(isFinal);
	}
	
	public void setAbstract(boolean isAbstract){
		new CheckBox("abstract").toggle(isAbstract);
	}
	
	public void setStatic(boolean isStatic){
		new CheckBox("static").toggle(isStatic);
	}
	
	public void setRegisterInBeans(boolean register){
		new CheckBox("Register in beans.xml").toggle(register);
	}
	
	public void setGenerateComments(boolean generate){
		new CheckBox("Generate comments").toggle(generate);
	}
	
	public void setDelegateFieldName(String name){
		new LabeledText("Delegate Field Name:").setText(name);
	}
	
	public String getName(){
		return new LabeledText("Name:").getText();
	}
	
	public void addDecoratedTypeInterfaces(String bindings){
		new PushButton("Add...").click();
		new DefaultShell("Implemented Interfaces Selection");
		new DefaultText(0).setText(bindings);
		new WaitUntil(new TableHasRows(new DefaultTable()));
		new PushButton("OK").click();
		new DefaultShell("New Decorator");
	}
	
	public List<TableItem> getDecoratedTypeInterfaces(){
		return new DefaultTable().getItems();
	}


}
