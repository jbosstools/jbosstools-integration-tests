package org.jboss.tools.cdi.reddeer.common.model.ui.editor;

import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.reddeer.workbench.impl.editor.AbstractEditor;
import org.jboss.tools.cdi.reddeer.common.model.ui.AddIfClassAvailableDialog;
import org.jboss.tools.cdi.reddeer.common.model.ui.AddIfSystemPropertyDialog;
import org.jboss.tools.cdi.reddeer.common.model.ui.AddIncludeExcludeDialog;
import org.jboss.tools.common.reddeer.label.IDELabel;

public class EditorPartWrapper extends AbstractEditor{
	
	public EditorPartWrapper(){
		super("beans.xml");
	}
	
	public void activateTreePage(){
		new DefaultCTabItem("Tree").activate();
	}
	
	public void activateSourcePage(){
		new DefaultCTabItem("Source").activate();
	}
	
	public void addInterceptors(String interceptorName){
		new DefaultTreeItem(new DefaultSection("beans"),"beans.xml","Interceptors").select();
		new PushButton(new DefaultSection("Interceptors"),"Add...").click();
		new DefaultShell("Add Class...");
		new DefaultText(0).setText(interceptorName);
		new PushButton("Finish").click();
	}
	
	public void removeInterceptors(String interceptorName){
		new DefaultTreeItem(new DefaultSection("beans"),"beans.xml","Interceptors").select();
		new DefaultTableItem(interceptorName).select();
		new PushButton(new DefaultSection("Interceptors"),"Remove...").click();
		new DefaultShell("Confirmation");
		new OkButton().click();
	}
	
	public void addDecorators(String decoratorName){
		new DefaultTreeItem(new DefaultSection("beans"),"beans.xml","Decorators").select();
		new PushButton(new DefaultSection("Decorators"),"Add...").click();
		new DefaultShell("Add Class...");
		new DefaultText(0).setText(decoratorName);
		new PushButton("Finish").click();
	}
	
	public void removeDecorators(String decoratorName){
		new DefaultTreeItem(new DefaultSection("beans"),"beans.xml","Decorators").select();
		new DefaultTableItem(decoratorName).select();
		new PushButton(new DefaultSection("Decorators"),"Remove...").click();
		new DefaultShell("Confirmation");
		new OkButton().click();
	}
	
	public void addClasses(String className){
		new DefaultTreeItem(new DefaultSection("beans"),"beans.xml","Alternatives").select();
		new PushButton(new DefaultSection("Classes"),"Add...").click();
		new DefaultShell("Add Class...");
		new DefaultText(0).setText(className);
		new PushButton("Finish").click();
	}
	
	public void removeClasses(String className){
		new DefaultTreeItem(new DefaultSection("beans"),"beans.xml","Alternatives").select();
		new DefaultTableItem(className).select();
		new PushButton(new DefaultSection("Classes"),"Remove...").click();
		new DefaultShell("Confirmation");
		new OkButton().click();
	}
	
	public void addStereotypes(String stereotypeName){
		new DefaultTreeItem(new DefaultSection("beans"),"beans.xml","Alternatives").select();
		new PushButton(new DefaultSection("Stereotypes"),"Add...").click();
		new DefaultShell("Add Stereotype...");
		new DefaultText(0).setText(stereotypeName);
		new PushButton("Finish").click();
	}
	
	public void removeStereotypes(String stereotypeName){
		new DefaultTreeItem(new DefaultSection("beans"),"beans.xml","Alternatives").select();
		new DefaultTableItem(1,stereotypeName).select();
		new PushButton(new DefaultSection("Stereotypes"),"Remove...").click();
		new DefaultShell("Confirmation");
		new OkButton().click();
	}
	
	public void newWeldScan() {
		new DefaultTreeItem(new DefaultSection("beans"), IDELabel.WebProjectsTree.BEANS_XML).select();
		new ContextMenu("New", "Weld", "Scan...").select();
	}
	
	public Table getIncludeExcludeTable() {
		new DefaultTreeItem(new DefaultSection("beans"), IDELabel.WebProjectsTree.BEANS_XML, "Scan").select();
		return new DefaultTable();
	}
	
	public void removeIncludeExclude(String name) {
		Table table = getIncludeExcludeTable();
		for (int i = 0; i < table.rowCount(); i++) {
			if (table.getItem(i).getText(1).equals(name)) {
				table.select(i);
				break;
			}
		}
		removeIncludeExcludeWithHandler();
	}
	
	public void editIncludeExclude(String oldName, String newName, boolean isRegular) {
		Table table = getIncludeExcludeTable();
		for (int i = 0; i < table.rowCount(); i++) {
			if (table.getItem(i).getText(1).equals(oldName)) {
				table.select(i);
				break;
			}
		};
		editIncludeExcludeValues(newName, isRegular);
	}
	
	private void removeIncludeExcludeWithHandler() {
		new PushButton("Remove...").click();
		new WaitUntil(new ShellWithTextIsAvailable("Confirmation"));
		new DefaultShell("Confirmation");
		new PushButton("OK").click();
	}
	
	private void editIncludeExcludeValues(String newName, boolean isRegular) {
		new PushButton("Edit...").click();
		/*
		 * Text labeled "Name:"; no direct common parent -> LabeledText can't be used
		 */
		new DefaultText(0).setText(newName);
		new CheckBox().toggle(isRegular);
	}
	
	public boolean isObjectInEditor(String... path) {
		try {
			new DefaultTreeItem(path);
			return true;
		} catch (SWTLayerException exc) {
			return false;
		}
	}
	
	public AddIfSystemPropertyDialog invokeAddIfSystemPropertyDialog(String property) {
		new DefaultTreeItem("beans.xml", "Scan", property).select();
		new ContextMenu("Add System Property").select();
		new WaitUntil(new ShellWithTextIsAvailable("Add If System Property"));
		new DefaultShell("Add If System Property");
		return new AddIfSystemPropertyDialog();
	}
	
	public AddIncludeExcludeDialog invokeAddIncludeExcludeDialog() {
		new DefaultTreeItem(IDELabel.WebProjectsTree.BEANS_XML, "Scan").select();
		new ContextMenu("Add Include/Exclude").select();
		new WaitUntil(new ShellWithTextIsAvailable("Add Include/Exclude"));
		new DefaultShell("Add Include/Exclude");
		return new AddIncludeExcludeDialog();
	}
	
	public AddIfClassAvailableDialog invokeAddClassAvailableDialog(String property) {
		new DefaultTreeItem("beans.xml", "Scan", property).select();
		new ContextMenu("Add Class Available").select();
		new WaitUntil(new ShellWithTextIsAvailable("Add If Class Available"));
		new DefaultShell("Add If Class Available");
		return new AddIfClassAvailableDialog();
	}

}
