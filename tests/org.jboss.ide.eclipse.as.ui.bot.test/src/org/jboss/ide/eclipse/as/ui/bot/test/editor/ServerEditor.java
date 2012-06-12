package org.jboss.ide.eclipse.as.ui.bot.test.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.ide.eclipse.as.ui.bot.test.entity.XMLConfiguration;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.view.ServersView;

public class ServerEditor {

	private ServersView serversView = new ServersView();
	
	private String name;
	
	public ServerEditor(String name) {
		super();
		this.name = name;
	}

	public void open(){
		serversView.openServerEditor(name);
	}
	
	public ServerLaunchConfiguration openLaunchConfiguration(){
		SWTBotFactory.getBot().hyperlink("Open launch configuration").click();
		SWTBotFactory.getBot().shell("Edit Configuration").activate();
		return new ServerLaunchConfiguration();
	}
	
	public String getWebPort(){
		return SWTBotFactory.getBot().textWithLabel("Web").getText();
	}
	
	public String getManagementPort(){
		return SWTBotFactory.getBot().textWithLabel("Management").getText();
	}
	
	public List<XMLConfiguration> getXMLConfiguration(String categoryName){
		SWTBotTreeItem server = serversView.findServerByName(name);
		server.expand();
		final SWTBotTreeItem category = server.expandNode("XML Configuration", categoryName);

		SWTBotFactory.getBot().waitUntil(new TreeItemLabelChangedCondition(category.getNode(0)));
		
		List<XMLConfiguration> configurations = new ArrayList<XMLConfiguration>();
		for (SWTBotTreeItem item : category.getItems()){
			String[] columns = item.getText().split("   ");
			configurations.add(new XMLConfiguration(columns[0].trim(), columns[1].trim()));
		}
		return configurations;
	}
	
	private static class TreeItemLabelChangedCondition implements ICondition {

		private String firstTimeText;
		
		private SWTBotTreeItem item;
		
		public TreeItemLabelChangedCondition(SWTBotTreeItem item) {
			super();
			this.item = item;
		}

		@Override
		public void init(SWTBot bot) {
			firstTimeText = item.getText();
		}
		
		@Override
		public boolean test() throws Exception {
			return !firstTimeText.equals(item.getText());
		}

		@Override
		public String getFailureMessage() {
			return "Expected the tree item's text to change";
		}
	}
}
