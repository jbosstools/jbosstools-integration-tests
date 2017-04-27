package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.topmenu.NewMenuWizard;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.condition.TableItemIsFound;

public class NewBeanCreationWizard extends NewMenuWizard{
	
	public static final String NAME="Bean";
	public static final String SHELL_TEXT = "New CDI Bean";
	
	public NewBeanCreationWizard(){
		super(SHELL_TEXT, CDIConstants.CDI_GROUP,NAME);
	}
	
	public void setPackage(String packageName){
		new LabeledText("Package:").setText(packageName);
	}
	
	public void setName(String name){
		new LabeledText("Name:").setText(name);
	}
	
	public String getName(){
		return new LabeledText("Name:").getText();
	}
	
	public String getPackage(){
		return new LabeledText("Package:").getText();
	}
	
	public void setPublic(boolean isPublic){
		new RadioButton("public").toggle(isPublic);
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
	
	public void setNamed(boolean named){
		new CheckBox("Add @Named").toggle(named);
	}
	
	public void setAlternative(boolean alternative){
		new CheckBox("Add @Alternative").toggle(alternative);
	}
	
	public boolean isAlternative(){
		return new CheckBox("Add @Alternative").isChecked();
	}
	
	public void setRegisterInBeans(boolean register){
		new CheckBox("Register in beans.xml").toggle(register);
	}
	
	public void setGenerateComments(boolean generate){
		new CheckBox("Generate comments").toggle(generate);
	}
	
	public void setBeanName(String name){
		new LabeledText("Bean Name:").setText(name);
	}
	
	public void setScope(String scope){
		new LabeledCombo("Scope:").setSelection(scope);
	}
	
	public void setSuperclass(String superclass){
		new LabeledText("Superclass:").setText(superclass);
	}
	
	public String getSuperclass(){
		return new LabeledText("Superclass:").getText();
	}
	
	
	public void addInterfaces(String bindings){
		new PushButton("Add...").click();
		new DefaultShell("Implemented Interfaces Selection");
		new DefaultText(0).setText(bindings);
		new WaitUntil(new TableItemIsFound(new DefaultTable(), bindings));
		new PushButton("OK").click();
		new DefaultShell("New CDI Bean");
	}
	
	public void addQualifier(String stereoptypes){
		new PushButton("Add").click();
		new DefaultShell("Select Qualifier Annotation Type");
		new DefaultText(0).setText(stereoptypes);
		new WaitUntil(new TableItemIsFound(new DefaultTable(), stereoptypes));
		new DefaultTableItem(new WithTextMatcher(new RegexMatcher(".*"+stereoptypes+".*"))).select();
		new PushButton("OK").click();
		new DefaultShell("New CDI Bean");
	}
}
