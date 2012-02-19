package org.jboss.tools.esb.ui.bot.tests.editor.action;

import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.esb.ui.bot.tests.editor.Assertions;
import org.jboss.tools.esb.ui.bot.tests.editor.ESBAction;
import org.jboss.tools.esb.ui.bot.tests.editor.ESBObject;
import org.jboss.tools.esb.ui.bot.tests.editor.ESBObjectDummy;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.widgets.SWTBotSection;

public class Notifier extends ESBAction {

	public Notifier() {
		super("Notifier","Routers","org.jboss.soa.esb.actions.Notifier");

	}
	
	protected void doEditing(SWTBotEditor editor, String... path) {
		SWTBotSection section = bot.section(editor.bot(),getSectionTitle());
		editTextProperty(editor, section.bot(), "Ok Method:", "okMethod", "method");
		editTextProperty(editor, section.bot(), "Exception Method:", "exceptionMethod", "method");
		section = bot.section(editor.bot(),"Notification Lists");
		section.bot().button(IDELabel.Button.ADD).click();
		SWTBotShell shell = bot.shell("Add Notification List...").activate();
		shell.bot().text().setText("list");
		shell.bot().button(IDELabel.Button.FINISH).click();
		String xpath = getBaseXPath()+getXpath()+"/property[@name='destinations']/NotificationList[@type='list']";
		Assertions.assertXmlContentExists(editor.toTextEditor().getText(), xpath);
		section = bot.section(editor.bot(),"Targets");
		editor.save();
		String[] notifiersPath = arrayAppend(path, "Notifier","list");
		addTarget(editor, xpath, notifiersPath);
		addNotifier(editor, new ESBObjectDummy("Notify Console", "NotifyConsole"), xpath, notifiersPath);
		addEmail(editor, xpath, notifiersPath);
		addNotifier(editor, new ESBObjectDummy("Notify Files", "NotifyFiles"), xpath, notifiersPath);
		addNotifier(editor, new ESBObjectDummy("Notify FTP", "NotifyFTP"), xpath, notifiersPath);
		addNotifier(editor, new ESBObjectDummy("Notify FTP List", "NotifyFTPList"), xpath, notifiersPath);
		addNotifier(editor, new ESBObjectDummy("Notify Queues", "NotifyQueues"), xpath, notifiersPath);
		addSQL(editor, xpath, notifiersPath);
		addNotifier(editor, new ESBObjectDummy("Notify TCP", "NotifyTCP"), xpath, notifiersPath);
		addNotifier(editor, new ESBObjectDummy("Notify Topics", "NotifyTopics"), xpath, notifiersPath);
		// FIXME full coverage of notifiers
		//fail("OK");
	}
	
	private void addTarget(SWTBotEditor editor,String xpath,String... path) {
		SWTBotTreeItem item = SWTEclipseExt.selectTreeLocation(editor.bot(), path);
		ContextMenuHelper.prepareTreeItemForContextMenu(editor.bot().tree(),item);
		ContextMenuHelper.clickContextMenu(editor.bot().tree(), IDELabel.Menu.NEW,"Target...");
		SWTBot shellBot = bot.shell("Add Target").bot();
		shellBot.text().setText("java.lang.Object");
		shellBot.button(IDELabel.Button.FINISH).click();
		editor.save();
		xpath+="/target[@class='java.lang.Object']";
		Assertions.assertXmlContentExists(editor.toTextEditor().getText(), xpath);
	}
	private void addNotifier(SWTBotEditor editor, ESBObject obj,String xpath,String... path) {
		SWTBotTreeItem item = SWTEclipseExt.selectTreeLocation(editor.bot(), path);
		ContextMenuHelper.prepareTreeItemForContextMenu(editor.bot().tree(),item);
		ContextMenuHelper.clickContextMenu(editor.bot().tree(), IDELabel.Menu.NEW,obj.getMenuLabel());
		editor.save();
		xpath+="/target[@class='"+obj.xmlName+"']";
		Assertions.assertXmlContentExists(editor.toTextEditor().getText(), xpath);
	}
	private void addEmail(SWTBotEditor editor,String xpath,String... path) {
		
		String xpathOrig = new String (xpath);
		
		SWTBotTreeItem item = SWTEclipseExt.selectTreeLocation(editor.bot(), path);
		ContextMenuHelper.prepareTreeItemForContextMenu(editor.bot().tree(),item);
		ContextMenuHelper.clickContextMenu(editor.bot().tree(), IDELabel.Menu.NEW,"Notify Email...");
		SWTBot shellBot = bot.shell("Notify Email...").bot();
		shellBot.text(0).setText("a");
		shellBot.text(1).setText("b");
		shellBot.text(2).setText("c");
		shellBot.button(IDELabel.Button.FINISH).click();
		editor.save();
		xpath+="/target[@class='NotifyEmail' and @from='a' and @sendTo='b' and @subject='c']";
		Assertions.assertXmlContentExists(editor.toTextEditor().getText(), xpath);
		
		/* ldimaggi */
		bot.textWithLabel("Host:").setText("redhat.com");
		bot.textWithLabel("Port:").setText("25");
		bot.textWithLabel("Username:").setText("QEuser");
		bot.textWithLabel("Password:").setText("thepas$w0rd");
		bot.textWithLabel("Auth:").setText("LDAP");

		bot.textWithLabel("Message:").setText("The message");
		bot.textWithLabel("Copy to:").setText("The copier");
		
		//xpathOrig+="/target[@auth='LDAP' and @class='NotifyEmail' and @from='a' and @host='redhat.com' and @password='thepas$w0rd' and @port='25' and @sendTo='b' and @subject='c' and username='QEuser']";
		xpathOrig+="/target[@auth='LDAP' and @class='NotifyEmail' and @from='a' and @host='redhat.com' and @sendTo='b' and @subject='c' and @password='thepas$w0rd' and @port='25' and @ccTo='The copier' and @message='The message' and @username='QEuser']";
		Assertions.assertXmlContentExists(editor.toTextEditor().getText(), xpathOrig);	
		
		bot.button("&Add...").click();		
		SWTBotTreeItem [] theItems = bot.tree().getAllItems();
		
		theItems[0].getNode("resources.jar").expand();
		theItems[0].getNode("resources.jar").getNode("META-INF").expand();		
		theItems[0].getNode("resources.jar").getNode("META-INF").getNode("MANIFEST.MF").select();
		bot.button("&Finish").click();
		
		editor.save();
	}
	
	
	private void addSQL(SWTBotEditor editor,String xpath,String... path) {
		SWTBotTreeItem item = SWTEclipseExt.selectTreeLocation(editor.bot(), path);
		ContextMenuHelper.prepareTreeItemForContextMenu(editor.bot().tree(),item);
		ContextMenuHelper.clickContextMenu(editor.bot().tree(), IDELabel.Menu.NEW,"Notify SQL Table...");
		SWTBot shellBot = bot.shell("Notify SQL Table...").bot();
		shellBot.text(1).setText("a");
		shellBot.text(2).setText("b");
		shellBot.text(3).setText("c");
		shellBot.button(IDELabel.Button.FINISH).click();
		editor.save();
		xpath+="/target[@class='NotifySQLTable' and @driver-class='a' and @connection-url='b' and @user-name='c']";
		Assertions.assertXmlContentExists(editor.toTextEditor().getText(), xpath);
	}
}
