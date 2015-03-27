package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.cdi.reddeer.condition.TableItemIsFound;

public class NewInterceptorCreationWizard extends NewWizardDialog{
	
	public static final String CATEGORY="CDI (Context and Dependency Injection)";
	public static final String NAME="Interceptor";
	
	public NewInterceptorCreationWizard(){
		super(CATEGORY,NAME);
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
	
	public void setSuperclass(String name){
		new LabeledText("Superclass:").setText(name);
	}
	
	public void setRegisterInBeans(boolean register){
		new CheckBox("Register in beans.xml").toggle(register);
	}
	
	public void setGenerateComments(boolean generate){
		new CheckBox("Generate comments").toggle(generate);
	}
	
	public void setAroundInvokeMethodName(String name){
		new LabeledText("Around Invoke Method Name:").setText(name);
	}
	
	
	public void addInterceptorBindings(String bindings){
		new PushButton("Add").click();
		new DefaultShell("Select Interceptor Binding Annotation Type");
		new DefaultText(0).setText(bindings);
		Table t = new DefaultTable();
		new WaitUntil(new TableItemIsFound(t, bindings));
		for(TableItem i: t.getItems()){
			if(i.getText().contains(bindings)){
				i.select();
				break;
			}
		}
		new PushButton("OK").click();
		new DefaultShell("New Interceptor");
	}

}
