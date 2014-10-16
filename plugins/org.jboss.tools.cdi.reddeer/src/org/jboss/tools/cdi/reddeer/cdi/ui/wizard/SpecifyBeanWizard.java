package org.jboss.tools.cdi.reddeer.cdi.ui.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewQualifierCreationWizard;
import org.jboss.tools.common.reddeer.label.IDELabel;

public class SpecifyBeanWizard {
	
	private static final String CREATE_NEW_QUALIFIER = "Create New Qualifier...";
	private List<String> availableQualifiers = null;
	private List<String> inBeanQualifiers = null;
	
	public SpecifyBeanWizard() {
		new DefaultShell("Specify CDI Bean for the Injection Point");
	}
	
	public void add() {
		new PushButton(IDELabel.Button.ADD_WITH_ARROW).click();
	}
	
	public void addAll() {
		new PushButton(IDELabel.Button.ADD_ALL).click();
	}
	
	public void remove() {
		new PushButton(IDELabel.Button.REMOVE_WITH_ARROW).click();
	}
	
	public void removeAll() {
		new PushButton(IDELabel.Button.REMOVE_ALL).click();
	}
	
	public void edit() {
		new PushButton(IDELabel.Button.EDIT_VALUE_WITH_DOTS).click();
	}
	
	public NewQualifierCreationWizard createNewQualifier(String name, String packageName) {
		new PushButton(CREATE_NEW_QUALIFIER).click();
		new DefaultShell("New Qualifier");
		return new NewQualifierCreationWizard();
	}
	
	public boolean canAdd() {
		return new PushButton(IDELabel.Button.ADD_WITH_ARROW).isEnabled();
	}
	
	public boolean canAddAll() {
		return new PushButton(IDELabel.Button.ADD_ALL).isEnabled();
	}
	
	public boolean canRemove() {
		return new PushButton(IDELabel.Button.REMOVE_WITH_ARROW).isEnabled();
	}
	
	public boolean canRemoveAll() {
		return new PushButton(IDELabel.Button.REMOVE_ALL).isEnabled();
	}
	
	public boolean canEdit() {
		return new PushButton(IDELabel.Button.EDIT_VALUE_WITH_DOTS).isEnabled();
	}
	
	public boolean canCreateNewQualifier() {
		return new PushButton(CREATE_NEW_QUALIFIER).isEnabled();
	}
	
	public List<String> getAvailableQualifiers() {
		availableQualifiers =  new ArrayList<String>();
		for(TableItem i: new DefaultTable().getItems()){
			availableQualifiers.add(i.getText());
		}
		return availableQualifiers;
	}
	
	public List<String> getInBeanQualifiers() {
		inBeanQualifiers =  new ArrayList<String>();
		for(TableItem i: new DefaultTable(1).getItems()){
			inBeanQualifiers.add(i.getText());
		}
		return inBeanQualifiers;
	}
	
	public void addQualifier(String qualifier) {
		selectAvailableQualifier(qualifier);
		add();		
	}
	
	public void removeQualifier(String qualifier) {
		selectInBeanQualifier(qualifier);
		remove();		
	}
	
	private void selectAvailableQualifier(String qualifier) {
		new DefaultTable(0).select(qualifier);
	} 
	
	private void selectInBeanQualifier(String qualifier) {
		new DefaultTable(1).select(qualifier);
	} 
	
	public void finish(){
		String textShell = new DefaultShell().getText();
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive(textShell));
	}

}
