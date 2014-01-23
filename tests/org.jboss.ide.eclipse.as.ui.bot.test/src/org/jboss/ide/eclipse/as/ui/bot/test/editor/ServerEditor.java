package org.jboss.ide.eclipse.as.ui.bot.test.editor;

import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.view.ServersView;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withText;

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

	protected void openSection(final String title){
		UIThreadRunnable.syncExec(new VoidResult() {

			@Override
			public void run() {
				Section section = (Section) SWTBotFactory.getBot().widget(allOf(widgetOfType(Section.class), withText("Timeouts")));;
				if (!section.isExpanded()){
					SWTBotFactory.getBot().hyperlink(title).click();
				}				
			}
		});
	}

	public ServerLaunchConfiguration openLaunchConfiguration(){
		SWTBotFactory.getBot().hyperlink("Open launch configuration").click();
		SWTBotFactory.getBot().shell("Edit Configuration").activate();
		return new ServerLaunchConfiguration();
	}

	public void setStartTimeout(int timeout){
		openSection("Timeouts");
		SWTBotFactory.getBot().spinnerWithLabel("Start (in seconds):").setSelection(timeout);
	}

	public void setStopTimeout(int timeout){
		openSection("Timeouts");
		SWTBotFactory.getBot().spinnerWithLabel("Stop (in seconds):").setSelection(timeout);
	}

	public String getStartupPoller(){
		return SWTBotFactory.getBot().comboBoxWithLabel("Startup Poller").getText();
	}

	public void setStartupPoller(String text){
		SWTBotFactory.getBot().comboBoxWithLabel("Startup Poller").setSelection(text);
	}

	public String getShutdownPoller(){
		return SWTBotFactory.getBot().comboBoxWithLabel("Shutdown Poller").getText();
	}

	public void setShutdownPoller(String text){
		SWTBotFactory.getBot().comboBoxWithLabel("Shutdown Poller").setSelection(text);
	}

	public String getWebPort(){
		return SWTBotFactory.getBot().textWithLabel("Web").getText();
	}

	public String getJNDIPort(){
		return SWTBotFactory.getBot().textWithLabel("JNDI").getText();
	}

	public String getJMXPort(){
		return SWTBotFactory.getBot().textWithLabel("JMX RMI").getText();
	}

	public String getManagementPort(){
		return SWTBotFactory.getBot().textWithLabel("Management").getText();
	}

	public void save(){
		SWTBotFactory.getBot().activeEditor().save();
	}
}
