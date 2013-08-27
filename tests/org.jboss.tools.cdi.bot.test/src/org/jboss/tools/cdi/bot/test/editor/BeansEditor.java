/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.editor;

import org.apache.log4j.Level;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.IEditorReference;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.cdi.reddeer.common.model.ui.AddIfClassAvailableDialog;
import org.jboss.tools.cdi.reddeer.common.model.ui.AddIfSystemPropertyDialog;
import org.jboss.tools.cdi.reddeer.common.model.ui.AddIncludeExcludeDialog;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.widgets.SWTBotMultiPageEditor;

/**
 * @author Lukas Jungmann
 */
public class BeansEditor extends SWTBotMultiPageEditor {
	
	public enum Item {
		INTERCEPTOR("Interceptors"), DECORATOR("Decorators"),
		CLASS("Alternatives"), STEREOTYPE("Alternatives");
		
		private final String node;
		
		private Item(String node) {
			this.node = node;
		}
		
		private String getNode() {
			return node;
		}
		
		public String getElementName() {
			switch (this) {
			case STEREOTYPE:
				return "stereotype";
			default:
				return "class";
			}
		}
	}
	
	private SWTBotExt bot = new SWTBotExt();
	private static final String ROOT_NODE = "beans.xml";

	public BeansEditor(IEditorReference editorReference, SWTWorkbenchBot bot) {
		super(editorReference, bot);
	}

	public BeansEditor add(Item item, String name) {
		return modify(item, name, "Add...", new AddDialogHandler(item, name));
	}
	
	public BeansEditor remove(Item item, String name) {
		return modify(item, name, "Remove...", new DeleteDialogHandler());
	}
	
	public String getSelectedItem() {
		return bot().tree().selection().get(0, 0);
	}
	
	public void newWeldScan() {
		new DefaultTreeItem(IDELabel.WebProjectsTree.BEANS_XML).select();
		new ContextMenu("New", "Weld", "Scan...").select();
	}
	
	public Table getIncludeExcludeTable() {
		activateScanRoot();
		return new DefaultTable();
	}
	
	public AddIncludeExcludeDialog invokeAddIncludeExcludeDialog() {
		activateScanRoot();
		new ContextMenu("Add Include/Exclude").select();
		new WaitUntil(new ShellWithTextIsActive("Add Include/Exclude"));
		return new AddIncludeExcludeDialog();
	}
	
	public void removeIncludeExclude(int index) {
		getIncludeExcludeTable().select(index);
		removeIncludeExcludeWithHandler();
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
	
	public void editIncludeExclude(int index, String newName, boolean isRegular) {
		getIncludeExcludeTable().select(index);
		editIncludeExcludeValues(newName, isRegular);
	}
	
	public AddIfClassAvailableDialog invokeAddClassAvailableDialog(String property) {
		new DefaultTreeItem("beans.xml", "Scan", property).select();
		new ContextMenu("Add Class Available").select();
		new WaitUntil(new ShellWithTextIsActive("Add If Class Available"));
		return new AddIfClassAvailableDialog();
	}
	
	public AddIfSystemPropertyDialog invokeAddIfSystemPropertyDialog(String property) {
		new DefaultTreeItem("beans.xml", "Scan", property).select();
		new ContextMenu("Add System Property").select();
		new WaitUntil(new ShellWithTextIsActive("Add If System Property"));
		return new AddIfSystemPropertyDialog();
	}
	
	public boolean isObjectInEditor(String... path) {
		try {
			new DefaultTreeItem(path);
			return true;
		} catch (SWTLayerException exc) {
			return false;
		}
	}
	
	private void editIncludeExcludeValues(String newName, boolean isRegular) {
		new PushButton("Edit...").click();
		/*
		 * Text labeled "Name:"; no direct common parent -> LabeledText can't be used
		 */
		new DefaultText(0).setText(newName);
		new CheckBox().toggle(isRegular);
	}
	
	private void activateScanRoot() {
		try {
			new DefaultTreeItem(IDELabel.WebProjectsTree.BEANS_XML, "Scan").select();
		} catch (SWTLayerException exc) {
			// problem when finding beans.xml tree
			// try to shell tree item with index 1
			new DefaultTreeItem(1, IDELabel.WebProjectsTree.BEANS_XML, "Scan").select();
		}
		
	}
	
	private void removeIncludeExcludeWithHandler() {
		new PushButton("Remove...").click();
		new WaitUntil(new ShellWithTextIsActive("Confirmation"));
		new PushButton("OK").click();
	}
	
	private BeansEditor modify(Item item, String name, String actionLabel, DialogHandler h) {
		SWTBotTree tree = bot.tree(2);
		for (SWTBotTreeItem ti:tree.getAllItems()) {
			log.setLevel(Level.FATAL);
			log.fatal(ti.getText());
		}
		tree.expandNode(ROOT_NODE, item.getNode()).select().click();
		selectItem(item, name);
		getItemButton(item, actionLabel).click();
		h.handle(bot.activeShell());
		bot.sleep(500);
		this.setFocus();
		return this;
	}
	
	private void selectItem(Item item, String name) {
		SWTBotTable t = item == Item.STEREOTYPE ? bot.table(1) : bot.table(0);
		if (t.containsItem(name)) {
			t.select(name);
		}
	}
	
	private SWTBotButton getItemButton(Item i, String label) {
		return i == Item.STEREOTYPE ? bot.button(label, 1) : bot.button(label, 0);
	}

	private interface DialogHandler {
		void handle(SWTBotShell dialog);
	}
	
	private class AddDialogHandler implements DialogHandler {
		
		private final Item type;
		private final String name;
		
		public AddDialogHandler(Item type, String name) {
			this.type = type;
			this.name = name;
		}
		
		public void handle(SWTBotShell dialog) {
			SWTBot sh = dialog.bot();
			SWTBotText t = type == Item.STEREOTYPE
				? sh.textWithLabel("Stereotype:*")
				: sh.textWithLabel("Class:*");
			t.setText(name);
			sh.button("Finish").click();
		}
	}
	
	private class DeleteDialogHandler implements DialogHandler {

		public void handle(SWTBotShell dialog) {
			dialog.bot().button("OK").click();
		}
		
	}
}
