package org.jboss.ide.eclipse.as.reddeer.server.editor;

import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServer;
import org.jboss.reddeer.eclipse.wst.server.ui.editor.ServerEditor;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.spinner.DefaultSpinner;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.core.matcher.WithLabelMatcher;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;

/**
 * Represents a server editor with entries specific for JBoss servers {@link JBossServer}
 * @author Lucia Jelinkova
 *
 */
public class JBossServerEditor extends ServerEditor {

	public JBossServerEditor(String title) {
		super(title);
	}

	public JBossServerLaunchConfiguration openLaunchConfiguration(){
		//		SWTBotFactory.getBot().hyperlink("Open launch configuration").click();
		//		SWTBotFactory.getBot().shell("Edit Configuration").activate();
		//		return new JBossServerLaunchConfiguration();
		throw new UnsupportedOperationException();
	}

	public void setStartTimeout(int timeout){
		openSection("Timeouts");
		new DefaultSpinner(new WithLabelMatcher("Start (in seconds):")).setValue(timeout);
	}

	public void setStopTimeout(int timeout){
		openSection("Timeouts");
		new DefaultSpinner(new WithLabelMatcher("Stop (in seconds):")).setValue(timeout);
	}

	public String getStartupPoller(){
		return new LabeledCombo("Startup Poller").getText();
	}

	public void setStartupPoller(String text){
		new LabeledCombo("Startup Poller").setText(text);
	}

	public String getShutdownPoller(){
		return new LabeledCombo("Shutdown Poller").getText();
	}

	public void setShutdownPoller(String text){
		new LabeledCombo("Shutdown Poller").setText(text);
	}

	public String getWebPort(){
		return new LabeledText("Web").getText();
	}

	public String getJNDIPort(){
		return new LabeledText("JNDI").getText();
	}

	public String getJMXPort(){
		return new LabeledText("JMX RMI").getText();
	}

	public String getManagementPort(){
		return new LabeledText("Management").getText();
	}
	
	protected void openSection(final String title){
		new DefaultSection(title).setExpanded(true);
	}
}
