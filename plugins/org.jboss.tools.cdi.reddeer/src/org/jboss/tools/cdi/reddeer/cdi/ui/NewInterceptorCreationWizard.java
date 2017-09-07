package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.condition.TableItemIsFound;

public class NewInterceptorCreationWizard extends NewMenuWizard{
	
	public static final String NAME="Interceptor";
	public static final String SHELL_TEXT = "New Interceptor";
	
	public NewInterceptorCreationWizard(){
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
