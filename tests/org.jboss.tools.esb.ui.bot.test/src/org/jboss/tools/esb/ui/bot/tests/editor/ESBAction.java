package org.jboss.tools.esb.ui.bot.tests.editor;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.finders.ContextMenuFinder;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.widgets.SWTBotSection;

public abstract class ESBAction extends ESBObject {

	public final String category;
	private final String className;
	private String service;

	public ESBAction(String uiName, String category, String className) {
		super(uiName, "action");
		this.category = category;
		this.className = className;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getService() {
		return service;
	}

	public String getClassName() {
		return className;
	}

	@Override
	public String getXpath() {
		return this.xmlName + "[@class='" + getClassName() + "' and @name='"
				+ this.uiName + "']";
	}

	@Override
	public String getBaseXPath() {
		return "//jbossesb/services/service[@name='" + getService()
				+ "']/actions/";
	}

	protected SWTBotShell openForm(SWTBotEditor editor, String... path) {
		editor.show();
		SWTBotTreeItem provItem = SWTEclipseExt.selectTreeLocation(
				editor.bot(), path);
		ContextMenuHelper.prepareTreeItemForContextMenu(editor.bot().tree(),
				provItem);
		if (category == null) {
			ContextMenuHelper.clickContextMenu(editor.bot().tree(),
					IDELabel.Menu.NEW, getMenuLabel());
		} else {
			ContextMenuHelper.clickContextMenu(editor.bot().tree(),
					IDELabel.Menu.NEW, category, getMenuLabel());
		}
		return new SWTBot().shell(getShellTitle());
	}

	protected void doFillForm(SWTBotShell shell) {
		Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()),
				false);
		shell.bot().text(0).setText(this.uiName);
		Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()),
				true);
	}

	protected void doEditing(SWTBotEditor editor, String... path) {
		// SWTBotSection section = bot.section(getSectionTitle());
	}

	@Override
	public String getSectionTitle() {
		return this.uiName + " Action";
	}

	protected void editProcess(SWTBotEditor editor, boolean combo) {
		SWTBotSection section = bot.section(editor.bot(), getSectionTitle());
		String value = "process";
		if (combo) {
			section.bot().comboBoxWithLabel("Process:").setSelection(0);
		} else {
			SWTBotText text = section.bot().textWithLabel("Process:");
			if (text.isEnabled())
				text.setText(value);
		}
		String text = editor.toTextEditor().getText();
		String xpath;
		if ("".equals(value)) {
			xpath = "count(" + getBaseXPath() + getXpath() + ")=1";
		} else {
			xpath = "count(" + getBaseXPath()
					+ getXpath().substring(0, getXpath().length() - 1)
					+ " and @process='" + value + "'])=1";
		}
		editor.save();
		Assertions.assertXmlContentBool(text, xpath);
	}

	public void edit(SWTBotEditor editor, String... path) {
		editor.show();
		List<String> newPath = new ArrayList<String>();
		for (String str : path) {
			newPath.add(str);
		}
		newPath.add(this.uiName);
		SWTEclipseExt.selectTreeLocation(editor.bot(),
				newPath.toArray(new String[] {}));
		doEditing(editor, path);
		editor.save();
	}

	public void create(SWTBotEditor editor, String... path) {
		SWTBotShell shell = openForm(editor, path);
		doFillForm(shell);
		shell.bot().button(getFinishButton()).click();
		String text = editor.toTextEditor().getText();
		String xpath = "count(" + getBaseXPath() + getXpath() + ")=1";
		Assertions.assertXmlContentBool(text, xpath);
		editor.save();
	}

}
