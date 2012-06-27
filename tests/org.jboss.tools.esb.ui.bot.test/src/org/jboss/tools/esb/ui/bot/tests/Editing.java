package org.jboss.tools.esb.ui.bot.tests;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.esb.ui.bot.tests.editor.Assertions;
import org.jboss.tools.esb.ui.bot.tests.editor.ESBAction;
import org.jboss.tools.esb.ui.bot.tests.editor.ESBActionFactory;
import org.jboss.tools.esb.ui.bot.tests.editor.ESBListener;
import org.jboss.tools.esb.ui.bot.tests.editor.ESBListenerFactory;
import org.jboss.tools.esb.ui.bot.tests.editor.ESBObject;
import org.jboss.tools.esb.ui.bot.tests.editor.ESBProvider;
import org.jboss.tools.esb.ui.bot.tests.editor.ESBProviderFactory;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.ESBESBFile;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.ESBESBProject;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.JavaProject;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.parts.ObjectMultiPageEditorBot;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;
import org.jboss.tools.ui.bot.ext.widgets.SWTBotSection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@Require(perspective = "Java", runOnce = true)
public class Editing extends SWTTestExt {

	public static String menu_addService = "Add Service...";
	public static String projectName = "ESB";
	public static String configFile = "jboss-esb";
	public static String configFileFull = configFile + ".xml";
	public static String configFileFullNotSaved = configFile + ".xml*";
	public static String node_globals = "Globals";
	public static String node_services = "Services";
	public static String node_providers = "Providers";

	@BeforeClass
	public static void setupProject() {
		SWTBot wiz = open.newObject(ActionItem.NewObject.ESBESBProject.LABEL);
		wiz.textWithLabel(ESBESBProject.TEXT_PROJECT_NAME).setText(projectName);
		wiz.sleep(30000l);
		wiz.button(IDELabel.Button.NEXT).click();
		wiz.sleep(30000l);
		wiz.button(IDELabel.Button.NEXT).click();
		wiz.sleep(30000l);
		open.finish(wiz);		
	}

	@AfterClass
	public static void waitAMinute() {
		//bot.sleep(Long.MAX_VALUE);
	}

	@Test
	public void createEsbFile() {
		packageExplorer.show().bot().tree().select(projectName);
		SWTBot wiz = open.newObject(ESBESBFile.LABEL);
		wiz.textWithLabel(ESBESBFile.TEXT_NAME).setText("another-esb-config");
		bot.sleep(30000l);
		open.finish(wiz);
		assertTrue(bot.editorByTitle("another-esb-config.xml") != null);
		assertTrue("ESB Editor opened problems",
				problems.getErrorsNode(bot) == null);
	}

	@Test
	public void providers() {
		List<String> providerList = getAvailableProviders();
		String[] actionPath = new String[] { configFileFull, node_providers };
		// first create all actions
		for (Method m : ESBProviderFactory.class.getMethods()) {

			if (m.getReturnType().equals(ESBProvider.class)) {
				try {
					log.info("Invoke " + m.getName());
					ESBProvider action = (ESBProvider) m.invoke(null, new Object[]{});
					bot.sleep(TIME_5S);
					action.create(getEditor(), actionPath);
					providerList.remove(action.getMenuLabel());
					bot.sleep(TIME_1S);
					Assertions.assertEmptyProblemsView("after "+action.getMenuLabel()+" was added");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		assertTrue(
				"Not all providers, covered, remaining are :"
						+ Arrays.toString(providerList.toArray()),
				providerList.isEmpty());
		collapseTree();
	}

	@Test
	public void listeners() {
		String service = "aaa";
		addService(service);
		List<String> listenerList = getAvailableListeners(service);
		String[] actionPath = new String[] { configFileFull, node_services,
				service, "Listeners" };
		for (Method m : ESBListenerFactory.class.getMethods()) {

			if (m.getReturnType().equals(ESBListener.class)) {
				try {
					log.info("Invoke " + m.getName());
					ESBListener action = (ESBListener) m.invoke(null, new Object[]{});
					action.setService(service);
					action.create(getEditor(), actionPath);
					listenerList.remove(action.getMenuLabel());
				} catch (Exception e) {
					fail("Exception "+e.getMessage());
					e.printStackTrace();
				}
			}
		}
		assertTrue(
				"Not all listeners, covered, remaining are :"
						+ Arrays.toString(listenerList.toArray()),
				listenerList.isEmpty());
		collapseTree();
	}

	@Test
	public void actions() {
		String service = "bbb";
		addService(service);
		String[] actionPath = new String[] { configFileFull, node_services,
				"bbb", "Actions" };
		// first create all actions
		for (Method m : ESBActionFactory.class.getMethods()) {

			if (m.getReturnType().equals(ESBAction.class)) {
				try {
					log.info("Invoke " + m.getName());
					ESBAction action = (ESBAction) m.invoke(null, new Object[]{});
					action.setService(service);	
					action.create(getEditor(), actionPath);
					//action.edit(getEditor(), actionPath);   /* Why is this here???   ldimaggi Nov 4 2011  */
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					fail(e.getMessage());
				}
			}
		}
		collapseTree();
		// then do editing on each
		for (Method m : ESBActionFactory.class.getMethods()) {

			if (m.getReturnType().equals(ESBAction.class)) {
				try {
					log.info("Invoke " + m.getName());
					ESBAction action = (ESBAction) m.invoke(null, new Object[]{});
					action.setService(service);
					action.edit(getEditor(), actionPath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private SWTBotEditor getEditor() {
		return bot.editorByTitle(configFileFull);
	}

	private void collapseTree() {
		getEditor().show();
		SWTEclipseExt.selectTreeLocation(getEditor().bot(), configFileFull)
				.collapse();
	}


	private List<String> getMenuItems(String menuItem, String... treeLocation) {
		final List<String> list = new ArrayList<String>();
		SWTBotEditor editor = getEditor();
		editor.show();
		SWTBotTreeItem provItem = SWTEclipseExt.selectTreeLocation(
				editor.bot(), treeLocation);
		ContextMenuHelper.prepareTreeItemForContextMenu(editor.bot().tree(),
				provItem);

		final SWTBotMenu menuRunAs = new SWTBotMenu(
				ContextMenuHelper.getContextMenu(editor.bot().tree(), menuItem,
						false));
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				int menuItemIndex = 0;
				MenuItem menuItem = null;
				final MenuItem[] menuItems = menuRunAs.widget.getMenu()
						.getItems();
				while (menuItem == null && menuItemIndex < menuItems.length) {
					String item = menuItems[menuItemIndex].getText();
					if (!"".equals(item)) {
						list.add(item);
					}
					menuItemIndex++;
				}

			}
		});
		return list;
	}

	private List<String> getAvailableProviders() {
		return getMenuItems(IDELabel.Menu.NEW, configFileFull, node_providers);
	}

	private List<String> getAvailableListeners(String service) {
		return getMenuItems(IDELabel.Menu.NEW, configFileFull, node_services,
				service, "Listeners");
	}

	private void addService(String name) {
		SWTBotEditor editor = getEditor();
		SWTBotTreeItem services = SWTEclipseExt.selectTreeLocation(
				editor.bot(), configFileFull, node_services);
		ContextMenuHelper.prepareTreeItemForContextMenu(editor.bot().tree(),
				services);
		new SWTBotMenu(ContextMenuHelper.getContextMenu(editor.bot().tree(),
				menu_addService, false)).click();
		SWTBotShell shell = bot.shell("Add Service");
		shell.activate();
		SWTBot shellBot = shell.bot();	
		assertFalse(bot.button(IDELabel.Button.FINISH).isEnabled());
		shellBot.text(0).setText(name);
		assertFalse(bot.button(IDELabel.Button.FINISH).isEnabled());
		shellBot.text(1).setText(name);
		assertFalse(bot.button(IDELabel.Button.FINISH).isEnabled());
		shellBot.text(2).setText(name);
		assertTrue(bot.button(IDELabel.Button.FINISH).isEnabled());
		open.finish(shellBot);
		
		/* New test - for SPA-P 5.3 new feature - recordRoute - */		
//		org.jboss.tools.ui.bot.ext.SWTUtilExt.displayAllBotWidgets(bot);
		assertTrue (bot.comboBoxWithLabel("Record Route:").selectionIndex() == -1);
		assertTrue (bot.comboBoxWithLabel("Record Route:").itemCount() == 3);
		String [] theItems = bot.comboBoxWithLabel("Record Route:").items();
		assertTrue (theItems.length == 3);
		assertTrue (theItems[1].equals("true"));
		assertTrue (theItems[2].equals("false"));
		bot.comboBoxWithLabel("Record Route:").setSelection("true");
		assertTrue (bot.comboBoxWithLabel("Record Route:").getText().equals("true"));		
		
		Assertions.assertXmlContentBool(getEditor().toTextEditor().getText(),
				"count(//jbossesb/services/service[@name='" + name + "' and @recordRoute='true'])=1");
		
		Assertions.assertTreeContent(getEditor(),configFileFullNotSaved, node_services, name);
		addPropertyWithXMLContent("//jbossesb/services/service[@name='" + name
				+ "']", configFileFullNotSaved, node_services, name);		
		editor.save();

	}

	/**
	 * adds property with several xml tags and attributes into item defined by
	 * given path
	 * 
	 * @param path
	 */
	private void addPropertyWithXMLContent(String xpathPath, String... path) {
		String propertyName = xpathPath.replaceAll("\\/|\\@|\\[|\\]|\\'|\\=",
				"");
		SWTBotEditor editor = getEditor();
		SWTEclipseExt.selectTreeLocation(editor.bot(), path);
		SWTBotSection section = bot.section("Properties");
		new SWTBotButton(bot.widget(widgetOfType(Button.class), section.widget))
				.click();
		SWTBotShell shell = bot.shell("Add Property").activate();
		Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.FINISH), false);
		shell.bot().text(0).setText(propertyName);
		shell.bot().text(1).setText("Value");
		Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.FINISH), true);
		open.finish(shell.bot());
		Assertions.assertXmlContentBool(getEditor().toTextEditor().getText(), "count("
				+ xpathPath + "/property[@name='" + propertyName + "'])=1");
		path[0] = configFileFullNotSaved; // until we save file
		SWTEclipseExt.selectTreeLocation(editor.bot(), path);
		section = bot.section("Properties");
		SWTBotTable table = new SWTBotTable(bot.widget(
				widgetOfType(Table.class), section.widget));
		assertTrue("Properties table does not containt recently added row",
				table.containsItem(propertyName));
		// property added and verified
		List<String> propPathList = new ArrayList<String>();
		for (String item : path) {
			propPathList.add(item);
		}
		propPathList.add(propertyName);
		String[] propPath = (String[]) propPathList.toArray(new String[] {});
		SWTEclipseExt.selectTreeLocation(editor.bot(), propPath);
		section = bot.section("Tags");
		new SWTBotButton(bot.widget(widgetOfType(Button.class), section.widget))
				.click();
		shell = bot.shell("Add Tag").activate();
		Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.FINISH), false);
		shell.bot().text().setText(propertyName);
		Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.FINISH), true);
		open.finish(shell.bot());
		Assertions.assertXmlContentBool(getEditor().toTextEditor().getText(), "count("
				+ xpathPath + "/property[@name='" + propertyName + "']/"
				+ propertyName + ")=1");
		propPathList.add(propertyName);
		String[] tagPath = (String[]) propPathList.toArray(new String[] {});
		SWTEclipseExt.selectTreeLocation(editor.bot(), tagPath);
		// tag added, now try attribute
		section = bot.section("Attributes");
		new SWTBotButton(bot.widget(widgetOfType(Button.class), section.widget))
				.click();
		shell = bot.shell("Add Attribute...").activate();
		Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.FINISH), false);
		shell.bot().text(0).setText("name");
		shell.bot().text(1).setText("value");
		Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.FINISH), true);
		open.finish(shell.bot());
		Assertions.assertXmlContentBool(getEditor().toTextEditor().getText(), "count("
				+ xpathPath + "/property[@name='" + propertyName + "']/"
				+ propertyName + "[@name='value'])=1");
		// add some content into tag's body
		section = bot.section("Body Content");
		SWTBotText text = new SWTBotText(bot.widget(widgetOfType(Text.class),
				section.widget));
		text.setFocus();
		
		text.setText("<>@&"); 
		getEditor().save();
		
		/* ldimaggi - https://issues.jboss.org/browse/JBQA-5829 */
//		Assertions.assertXmlContentString(getEditor().toTextEditor().getText(), xpathPath
//				+ "/property[@name='" + propertyName + "']/" + propertyName 
//		+ "[@name='value']/text()", "<>@&");

	}





}
