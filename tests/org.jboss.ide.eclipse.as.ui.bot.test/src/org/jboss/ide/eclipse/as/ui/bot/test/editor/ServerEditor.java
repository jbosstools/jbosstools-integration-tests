package org.jboss.ide.eclipse.as.ui.bot.test.editor;

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
}
