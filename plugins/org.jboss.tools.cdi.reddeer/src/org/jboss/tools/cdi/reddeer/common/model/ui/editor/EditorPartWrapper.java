package org.jboss.tools.cdi.reddeer.common.model.ui.editor;

import java.util.List;

import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
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
		selectBeanXmlType("Interceptors");
		new PushButton(new DefaultSection("Interceptors"),"Add...").click();
		new DefaultShell("Add Class...");
		new DefaultText(0).setText(interceptorName);
		new PushButton("Finish").click();
	}
	
	public void removeInterceptors(String interceptorName){
		selectBeanXmlType("Interceptors");
		new DefaultTableItem(interceptorName).select();
		new PushButton(new DefaultSection("Interceptors"),"Remove...").click();
		new DefaultShell("Confirmation");
		new OkButton().click();
	}
	
	public void addDecorators(String decoratorName){
		selectBeanXmlType("Decorators");
		new PushButton(new DefaultSection("Decorators"),"Add...").click();
		new DefaultShell("Add Class...");
		new DefaultText(0).setText(decoratorName);
		new PushButton("Finish").click();
	}
	
	public void removeDecorators(String decoratorName){
		selectBeanXmlType("Decorators");
		new DefaultTableItem(decoratorName).select();
		new PushButton(new DefaultSection("Decorators"),"Remove...").click();
		new DefaultShell("Confirmation");
		new OkButton().click();
	}
	
	public void addClasses(String className){
		selectBeanXmlType("Alternatives");
		new PushButton(new DefaultSection("Classes"),"Add...").click();
		new DefaultShell("Add Class...");
		new DefaultText(0).setText(className);
		new PushButton("Finish").click();
	}
	
	public void removeClasses(String className){
		selectBeanXmlType("Alternatives");
		new DefaultTableItem(className).select();
		new PushButton(new DefaultSection("Classes"),"Remove...").click();
		new DefaultShell("Confirmation");
		new OkButton().click();
	}
	
	public void addStereotypes(String stereotypeName){
		selectBeanXmlType("Alternatives");
		new PushButton(new DefaultSection("Stereotypes"),"Add...").click();
		new DefaultShell("Add Stereotype...");
		new DefaultText(0).setText(stereotypeName);
		new PushButton("Finish").click();
	}
	
	public void removeStereotypes(String stereotypeName){
		selectBeanXmlType("Alternatives");
		new DefaultTable(1).select(stereotypeName);
		new PushButton(new DefaultSection("Stereotypes"),"Remove...").click();
		new DefaultShell("Confirmation");
		new OkButton().click();
	}
	
	public void newWeldScan() {
		selectBeanXmlType("beans.xml");
		new ContextMenu("New", "Weld", "Weld Scan...").select();
	}
	
	public Table getIncludeExcludeTable() {
		selectBeanXmlType("Scan");
		return new DefaultTable();
	}
	
	public Table getWeldIncludeExcludeTable() {
		selectBeanXmlType("Weld Scan");
		return new DefaultTable();
	}
	
	private void selectBeanXmlType(String type){
		List<TreeItem> items = new DefaultTree(new DefaultSection("beans")).getAllItems();
		boolean found = false;
		for(TreeItem i: items){
			if(i.getText().equals(type)){
				i.select();
				found = true;
				break;
			}
		}
		if(!found){
			throw new EclipseLayerException("Unable to select "+type+" in beans.xml editor");
		}
	}
	
	public void removeWeldIncludeExclude(String name){
		Table table = getWeldIncludeExcludeTable();
		removeDefaultIncludeExclude(name, table);
		removeIncludeExcludeWithHandler();
		
	}
	
	
	public void removeIncludeExclude(String name) {
		Table table = getIncludeExcludeTable();
		removeDefaultIncludeExclude(name, table);
		removeIncludeExcludeWithHandler();
	}
	
	private void removeDefaultIncludeExclude(String name, Table table){
		boolean found =false;
		for (int i = 0; i < table.rowCount(); i++) {
			if (table.getItem(i).getText(1).equals(name)) {
				table.select(i);
				found = true;
				break;
			}
		}
		if(!found){
			throw new EclipseLayerException("Unable to find "+name);
		}
	}
	
	public void editIncludeExclude(String oldName, String newName, boolean isRegular) {
		Table table = getIncludeExcludeTable();
		editDefaultIncludeExclude(oldName, newName, isRegular, table);
		editIncludeExcludeValues(newName, isRegular);
	}
	
	public void editWeldIncludeExclude(String oldName, String newName, boolean isRegular) {
		Table table = getWeldIncludeExcludeTable();
		editDefaultIncludeExclude(oldName, newName, isRegular, table);
		editIncludeExcludeValues(newName, isRegular);
	}
	
	private void editDefaultIncludeExclude(String oldName, String newName, boolean isRegular,Table table) {
		boolean found =false;
		for (int i = 0; i < table.rowCount(); i++) {
			if (table.getItem(i).getText(1).equals(oldName)) {
				table.select(i);
				found = true;
				break;
			}
		};
		if(!found){
			throw new EclipseLayerException("Unable to find "+oldName);
		}
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
		} catch (CoreLayerException exc) {
			return false;
		}
	}
	
	public AddIfSystemPropertyDialog invokeAddIfSystemPropertyDialog(String property) {
		new DefaultTreeItem("beans.xml", "Scan", property).select();
		return invokeDefaultAddIfSystemPropertyDialog(property);
	}
	
	public AddIfSystemPropertyDialog invokeWeldAddIfSystemPropertyDialog(String property) {
		new DefaultTreeItem("beans.xml", "Weld Scan", property).select();
		return invokeDefaultAddIfSystemPropertyDialog(property);
	}
	
	private AddIfSystemPropertyDialog invokeDefaultAddIfSystemPropertyDialog(String property){
		new ContextMenu("Add System Property").select();
		new WaitUntil(new ShellWithTextIsAvailable("Add If System Property"));
		new DefaultShell("Add If System Property");
		return new AddIfSystemPropertyDialog();
	}
	
	public AddIncludeExcludeDialog invokeAddIncludeExcludeDialog() {
		new DefaultTreeItem(IDELabel.WebProjectsTree.BEANS_XML, "Scan").select();
		return invokeDefaultAddIncludeExcludeDialog();
	}
	
	public AddIncludeExcludeDialog invokeWeldAddIncludeExcludeDialog() {
		new DefaultTreeItem(IDELabel.WebProjectsTree.BEANS_XML, "Weld Scan").select();
		return invokeDefaultAddIncludeExcludeDialog();
	}
	
	private AddIncludeExcludeDialog invokeDefaultAddIncludeExcludeDialog(){
		new ContextMenu("Add Include/Exclude").select();
		new WaitUntil(new ShellWithTextIsAvailable("Add Include/Exclude"));
		new DefaultShell("Add Include/Exclude");
		return new AddIncludeExcludeDialog();
	}
	
	public AddIfClassAvailableDialog invokeAddClassAvailableDialog(String property) {
		new DefaultTreeItem("beans.xml", "Scan", property).select();
		return invokeDefaultWeldAddClassAvailableDialog(property);
	}
	
	public AddIfClassAvailableDialog invokeWeldAddClassAvailableDialog(String property) {
		new DefaultTreeItem("beans.xml", "Weld Scan", property).select();
		return invokeDefaultWeldAddClassAvailableDialog(property);
		
	}
	
	private AddIfClassAvailableDialog invokeDefaultWeldAddClassAvailableDialog(String property){
		new ContextMenu("Add Class Available").select();
		new WaitUntil(new ShellWithTextIsAvailable("Add If Class Available"));
		new DefaultShell("Add If Class Available");
		return new AddIfClassAvailableDialog();
	}
	
	//"Bean-Discovery-Mode:"
	//return new LabeledCombo(new DefaultSection("Cdi Beans"),"Bean-Discovery-Mode:")
	
	public void setBeanDiscoveryMode(String mode){
		selectBeanXmlType("beans.xml");
		new DefaultCombo(new DefaultSection("Cdi Beans"),0).setText(mode);
	}
	
	public void selectBeanDiscoveryMode(String mode){
		selectBeanXmlType("beans.xml");
		new DefaultCombo(new DefaultSection("Cdi Beans"),0).setSelection(mode);
	}
	
	public String getBeanDiscoveryMode(){
		selectBeanXmlType("beans.xml");
		return new DefaultCombo(new DefaultSection("Cdi Beans"),0).getText();
	}
	
	public boolean isBeanDiscoveryModeEnabled(){
		selectBeanXmlType("beans.xml");
		return new DefaultCombo(new DefaultSection("Cdi Beans"),0).isEnabled();
	}
	
	public List<String> getBeanDiscoveryModes(){
		selectBeanXmlType("beans.xml");
		return new DefaultCombo(new DefaultSection("Cdi Beans"),0).getItems();
	}
	
	public String getVersion(){
		selectBeanXmlType("beans.xml");
		return new DefaultText(new DefaultSection("Cdi Beans"),1).getText();
	}
	
	public boolean isVersionEnabled(){
		selectBeanXmlType("beans.xml");
		return new DefaultText(new DefaultSection("Cdi Beans"),1).isEnabled();
	}
	
	public String getName(){
		selectBeanXmlType("beans.xml");
		return new DefaultText(new DefaultSection("Cdi Beans"),0).getText();
	}
	
	public boolean isNameEnabled(){
		selectBeanXmlType("beans.xml");
		return new DefaultText(new DefaultSection("Cdi Beans"), 0).isEnabled();
	}

}
